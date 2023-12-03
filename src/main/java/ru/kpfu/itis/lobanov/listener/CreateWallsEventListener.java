package ru.kpfu.itis.lobanov.listener;

import ru.kpfu.itis.lobanov.exceptions.EventListenerException;
import ru.kpfu.itis.lobanov.model.environment.Cell;
import ru.kpfu.itis.lobanov.model.environment.Maze;
import ru.kpfu.itis.lobanov.model.net.Message;
import ru.kpfu.itis.lobanov.server.Server;
import ru.kpfu.itis.lobanov.utils.GameMessageProvider;
import ru.kpfu.itis.lobanov.utils.MessageType;

import java.nio.ByteBuffer;

public class CreateWallsEventListener extends AbstractEventListener {
    private final Maze maze = new Maze();
    private ByteBuffer buffer;

    @Override
    public void init(Server server) {
        super.init(server);
//        drawWalls();
    }

    @Override
    public void handle(Message message, int connectionId, int info) throws EventListenerException {
        if (!isInit) throw new EventListenerException("Listener hasn't been initialized yet.");

        Message response = GameMessageProvider.createMessage(MessageType.CREATE_WALLS_RESPONSE, server.getWallsBuffer().array());
        server.sendMessage(connectionId, response);
    }

    @Override
    public int getType() {
        return MessageType.CREATE_WALLS_REQUEST;
    }

    private void drawWalls() {
//        Cell[][] cells = maze.getData();
//        for (int i = 0; i < cells.length; i++) {
//            HBox line = new HBox();
//            line.setAlignment(Pos.CENTER);
//            for (int j = 0; j < cells.length; j++) {
//                if (cells[i][j].isWall()) {
//                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//                    line.getChildren().addAll(rectangle);
//                    walls.add(rectangle);
//                } else {
//                    Rectangle rectangle = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
//                    rectangle.setFill(Color.WHITE);
//                    line.getChildren().addAll(rectangle);
//                }
//            }
//            gameField.getChildren().addAll(line);
//        }
        byte[] b = new byte[maze.getWalls().size() * 2 * 4];
        buffer = ByteBuffer.wrap(b);
        Cell[][] cells = maze.getData();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j].isWall()) {
                    buffer.putInt(i);
                    buffer.putInt(j);
                }
            }
        }
    }
}
