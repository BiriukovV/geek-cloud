package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import network.Net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private File clientDir;

    private Net net;

    public ListView<String> view;

    public TextField input;

    private void readListFiles() {
        try {
//            view.getItems().clear();
//            Long filesCount = net.readLong();
//            for (int i = 0; i < filesCount; i++) {
//                String fileName = net.readUtf();
//                view.getItems().addAll(fileName);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read() {
        try {
            while (true) {
                String command = net.readUtf();
                if (command.equals("#list#")) {
                    readListFiles();
                }
                if(command.equals("#status#")){
                    String status = net.getInputStream().readUTF();
                    input.setText(status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clientDir = new File("files");
            net = new Net("localhost", 8189);
            Thread readThread = new Thread(this::read);
            readThread.setDaemon(true);
            readThread.start();

            // get client files with Io or Nio and put into clientView
            view.getItems().addAll(getClientFiles());
            view.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    String fileName = view.getSelectionModel().getSelectedItem();
                    try {
                        sendFile(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getClientFiles(){
        String[] files = clientDir.list();
        if(files == null){
            return List.of();
        }
        return  Arrays.stream(clientDir.list())
                .toList();
    }

    private void sendFile(String fileName) throws IOException {
        //   send protocol command
        net.getOutputStream().writeUTF("#file#");
        // send file name
        net.getOutputStream().writeUTF(fileName);

        // get file from client
        File file = clientDir.toPath().resolve(fileName).toFile();

        // send file length in bytes
        net.getOutputStream().writeLong(file.length());

        // allocate buffer
        byte[] buffer = new byte[256];

        // send file bytes
        try(InputStream fis = new FileInputStream(file)){
            while (fis.available() > 0){
                int readCount = fis.read(buffer);
                net.getOutputStream().write(buffer,0,readCount);
            }
        }
        net.getOutputStream().flush();

    }
}
