package org.spring.oopfinalexam.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.spring.oopfinalexam.model.Account;
import org.spring.oopfinalexam.model.AccountType;

@Repository
public class AccountRepository {

    private final DatabaseConnection databaseConnection;

    public AccountRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public Optional<Account> findById(UUID id) {
        String sql = "SELECT id, account_type FROM accounts WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch account " + id, e);
        }
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT 1 FROM accounts WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check account " + id, e);
        }
    }

    private Account mapRow(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(UUID.fromString(resultSet.getString("id")));
        account.setAccountType(AccountType.valueOf(resultSet.getString("account_type")));
        return account;
    }
}
