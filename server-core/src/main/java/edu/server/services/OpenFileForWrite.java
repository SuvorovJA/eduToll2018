package edu.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;


/*
 * открыть файл на запись на всё время работы программы, использовать буфер
 * сброс на диск каждые 20 строк
 * файл создаётся во временной папке
 * типа: /tmp/receivedpoints_4561794685426215327.txt
 *          %Temp%\receivedpoints_5719974674726407826.txt
 * путь и имя будет в логе
 */

@Service
public class OpenFileForWrite {

    private static final Logger log = LoggerFactory.getLogger(OpenFileForWrite.class);

    private String fileName = "receivedpoints_"; // обойдемся без параметров spring
    private String fileSuffix = ".txt";

    private BufferedWriter bufferedWriter;
    private int flushCounter = 0;

    public OpenFileForWrite() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(fileName, fileSuffix);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("FILE FOR RECEIVED POINTS = " + tempFile.getAbsolutePath());
        try {
            bufferedWriter = Files.newBufferedWriter(tempFile.toPath(), Charset.forName("utf-8"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void writeln(String s) {
        try {
            this.bufferedWriter.write(s);
            this.bufferedWriter.newLine();
            flushCounter++;
            if(flushCounter==20){
                this.bufferedWriter.flush();
                flushCounter=0;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    protected void finalize() throws Throwable {
        bufferedWriter.flush();
        bufferedWriter.close();
        super.finalize();
    }

}
