package org.spring.oopfinalexam.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.spring.oopfinalexam.model.Transaction;
import org.spring.oopfinalexam.model.TransactionType;

@Repository
public class TransactionRepository {

    private final DatabaseConnection databaseConnection;

    public TransactionRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Transaction> findAll(TransactionType type) {
        String sql = "SELECT id, account_id, created_at, transaction_type, amount, reason FROM transactions"
                + (type != null ? " WHERE transaction_type = ?" : "")
                + " ORDER BY created_at DESC";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (type != null) {
                statement.setString(1, type.name());
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch transactions", e);
        }
    }

    public List<Transaction> findByAccountId(UUID accountId) {
        String sql = "SELECT id, account_id, created_at, transaction_type, amount, reason "
                + "FROM transactions WHERE account_id = ? ORDER BY created_at DESC";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch transactions for account " + accountId, e);
        }
    }

    public Transaction save(Transaction transaction) {
        String sql = "INSERT INTO transactions (id, account_id, created_at, transaction_type, amount, reason) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, transaction.getId());
            statement.setObject(2, transaction.getAccountId());
            statement.setTimestamp(3, Timestamp.from(transaction.getCreatedAt()));
            statement.setString(4, transaction.getTransactionType().name());
            statement.setBigDecimal(5, transaction.getAmount());
            statement.setString(6, transaction.getReason());
            statement.executeUpdate();
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction", e);
        }
    }

    public BigDecimal calculateBalance(UUID accountId) {
        String sql = "SELECT COALESCE(SUM(CASE WHEN transaction_type = 'IN' THEN amount ELSE -amount END), 0) "
                + "AS balance FROM transactions WHERE account_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to calculate balance for account " + accountId, e);
        }
    }

    private List<Transaction> mapRows(ResultSet resultSet) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            transactions.add(mapRow(resultSet));
        }
        return transactions;
    }

    private Transaction mapRow(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.fromString(resultSet.getString("id")));
        transaction.setAccountId(UUID.fromString(resultSet.getString("account_id")));
        transaction.setCreatedAt(resultSet.getTimestamp("created_at").toInstant());
        transaction.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setReason(resultSet.getString("reason"));
        return transaction;
    }
}
