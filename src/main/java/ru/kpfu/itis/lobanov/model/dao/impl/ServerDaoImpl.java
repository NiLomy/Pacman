package ru.kpfu.itis.lobanov.model.dao.impl;

import ru.kpfu.itis.lobanov.exceptions.DbException;
import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;
import ru.kpfu.itis.lobanov.utils.db.DatabaseConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerDaoImpl implements ServerDao {
    private final Connection connection = DatabaseConnectionProvider.getConnection();
    public static final String ID_COLUMN = "id";
    public static final String HOST_COLUMN = "host";
    public static final String PORT_COLUMN = "port";
    public static final String IS_GAME_HELD_COLUMN = "is_game_held";
    public static final String GAME_MAP_COLUMN = "game_map";
    public static final String PLAYERS_COUNT_COLUMN = "players_count";
    public static final String GET_SINGLE_BY_ID_QUERY = "SELECT * from servers where id = ?";
    public static final String GET_SINGLE_BY_HOST_AND_PORT_QUERY = "SELECT * from servers where host=? AND port=?";
    public static final String GET_ALL_QUERY = "SELECT * from servers";
    public static final String GET_ALL_BY_HOST_QUERY = "SELECT * from servers WHERE host=?";
    public static final String INSERT_QUERY = "insert into servers (host, port, is_game_held, game_map, players_count) values (?, ?, ?, ?, ?);";
    public static final String UPDATE_QUERY = "update servers set host=?, port=?, is_game_held=?, game_map=?, players_count=? where id=?;";
    public static final String UPDATE_IS_GAME_HELD_QUERY = "update servers set is_game_held=? where host=? AND port=?;";
    ;
    public static final String DELETE_BY_ID_QUERY = "delete from servers where id=?;";
    public static final String DELETE_BY_HOST_AND_PORT_QUERY = "delete from servers where host=? and port=?;";

    @Override
    public ServerModel get(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_SINGLE_BY_ID_QUERY);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    return new ServerModel(
                            resultSet.getLong(ID_COLUMN),
                            resultSet.getString(HOST_COLUMN),
                            resultSet.getInt(PORT_COLUMN),
                            resultSet.getBoolean(IS_GAME_HELD_COLUMN),
                            resultSet.getString(GAME_MAP_COLUMN),
                            resultSet.getInt(PLAYERS_COUNT_COLUMN)
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(LogMessages.GET_SINGLE_SERVER_DB_EXCEPTION, e);
        }
    }

    @Override
    public List<ServerModel> getAll() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY);

            return getAllFromServerByResultSet(resultSet);
        } catch (SQLException e) {
            throw new DbException(LogMessages.GET_ALL_SERVERS_DB_EXCEPTION, e);
        }
    }

    @Override
    public void save(ServerModel serverModel) {
        try {
            int position = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(position++, serverModel.getHost());
            preparedStatement.setInt(position++, serverModel.getPort());
            preparedStatement.setBoolean(position++, serverModel.isGameHeld());
            preparedStatement.setString(position++, serverModel.getGameMap());
            preparedStatement.setInt(position++, serverModel.getPlayersCount());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(LogMessages.SAVE_DB_EXCEPTION, e);
        }
    }

    @Override
    public void update(ServerModel serverModel, int id) {
        try {
            int position = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(position++, serverModel.getHost());
            preparedStatement.setInt(position++, serverModel.getPort());
            preparedStatement.setBoolean(position++, serverModel.isGameHeld());
            preparedStatement.setString(position++, serverModel.getGameMap());
            preparedStatement.setInt(position++, serverModel.getPlayersCount());
            preparedStatement.setInt(position++, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(LogMessages.UPDATE_DB_EXCEPTION, e);
        }
    }

    @Override
    public void remove(ServerModel serverModel) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_QUERY);
            preparedStatement.setLong(1, serverModel.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(LogMessages.REMOVE_DB_EXCEPTION, e);
        }
    }

    @Override
    public ServerModel get(String host, int port) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_SINGLE_BY_HOST_AND_PORT_QUERY);
            int position = 1;
            preparedStatement.setString(position++, host);
            preparedStatement.setInt(position++, port);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    return new ServerModel(
                            resultSet.getLong(ID_COLUMN),
                            resultSet.getString(HOST_COLUMN),
                            resultSet.getInt(PORT_COLUMN),
                            resultSet.getBoolean(IS_GAME_HELD_COLUMN),
                            resultSet.getString(GAME_MAP_COLUMN),
                            resultSet.getInt(PLAYERS_COUNT_COLUMN)
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(LogMessages.GET_SINGLE_SERVER_DB_EXCEPTION, e);
        }
    }

    @Override
    public List<ServerModel> getAllFromServer(String host) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_HOST_QUERY);
            preparedStatement.setString(1, host);
            ResultSet resultSet = preparedStatement.executeQuery();

            return getAllFromServerByResultSet(resultSet);
        } catch (SQLException e) {
            throw new DbException(LogMessages.GET_ALL_SERVERS_DB_EXCEPTION, e);
        }
    }

    @Override
    public void updateGameStatus(String host, int port, boolean isGameHeld) {
        try {
            int position = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_IS_GAME_HELD_QUERY);
            preparedStatement.setBoolean(position++, isGameHeld);
            preparedStatement.setString(position++, host);
            preparedStatement.setInt(position++, port);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(LogMessages.UPDATE_DB_EXCEPTION, e);
        }
    }

    private List<ServerModel> getAllFromServerByResultSet(ResultSet resultSet) throws SQLException {
        List<ServerModel> servers = new ArrayList<>();

        if (resultSet != null) {
            while (resultSet.next()) {
                servers.add(
                        new ServerModel(
                                resultSet.getLong(ID_COLUMN),
                                resultSet.getString(HOST_COLUMN),
                                resultSet.getInt(PORT_COLUMN),
                                resultSet.getBoolean(IS_GAME_HELD_COLUMN),
                                resultSet.getString(GAME_MAP_COLUMN),
                                resultSet.getInt(PLAYERS_COUNT_COLUMN)
                        )
                );
            }
        }
        return servers;
    }

    @Override
    public void remove(String host, int port) {
        try {
            int position = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_HOST_AND_PORT_QUERY);
            preparedStatement.setString(position++, host);
            preparedStatement.setInt(position++, port);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(LogMessages.REMOVE_DB_EXCEPTION, e);
        }
    }
}
