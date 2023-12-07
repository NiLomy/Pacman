package ru.kpfu.itis.lobanov.model.dao.impl;

import ru.kpfu.itis.lobanov.exceptions.DbException;
import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;
import ru.kpfu.itis.lobanov.utils.DatabaseConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerDaoImpl implements ServerDao {
    private final Connection connection = DatabaseConnectionProvider.getConnection();

    @Override
    public ServerModel get(int id) {
        try {
            String sql = "SELECT * from servers where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    return new ServerModel(
                            resultSet.getLong("id"),
                            resultSet.getString("host"),
                            resultSet.getInt("port")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Can't get server from DB.", e);
        }
    }

    @Override
    public List<ServerModel> getAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * from servers";
            ResultSet resultSet = statement.executeQuery(sql);
            List<ServerModel> servers = new ArrayList<>();

            if (resultSet != null) {
                while (resultSet.next()) {
                    servers.add(
                            new ServerModel(
                                    resultSet.getLong("id"),
                                    resultSet.getString("host"),
                                    resultSet.getInt("port")
                            )
                    );
                }
            }
            return servers;
        } catch (SQLException e) {
            throw new DbException("Can't get server list from DB.", e);
        }
    }

    @Override
    public void save(ServerModel serverModel) {
        String sql = "insert into servers (host, port) values (?, ?);";
        try {
            int position = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(position++, serverModel.getHost());
            preparedStatement.setInt(position++, serverModel.getPort());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't save server into DB.", e);
        }
    }

    @Override
    public void update(ServerModel serverModel, int id) {
        String sql = "update servers set host=?, port=? where id=?;";
        try {
            int position = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(position++, serverModel.getHost());
            preparedStatement.setInt(position++, serverModel.getPort());
            preparedStatement.setInt(position++, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't update server into DB.", e);
        }
    }

    @Override
    public void remove(ServerModel serverModel) {
        String sql = "delete from servers where id=?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, serverModel.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't delete server from DB.", e);
        }
    }

    @Override
    public void remove(String host, int port) {
        String sql = "delete from servers where host=? and port=?;";
        try {
            int position = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(position++, host);
            preparedStatement.setInt(position++, port);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't delete server from DB.", e);
        }
    }
}