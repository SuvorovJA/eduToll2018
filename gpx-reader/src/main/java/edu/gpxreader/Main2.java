// TEST-DRIVE-PROJECT. no Spring integration
package edu.gpxreader;

public class Main2 {
    // TODO - переделать в тест
    // вывод файла используя class DeviceEmulator;

    public static void main(String[] args) {
        DeviceEmulator de = new DeviceEmulator("/11060.gpx");
        String firstline = de.getNext();
        String currline = "";
        System.out.println(firstline);
        for (int i=1;i<3000;++i){ // для 1441 точек из файла должно будет два повтора
            currline = de.getNext();
            if(firstline.equals(currline)){
                System.out.println(currline);
            }else {
                System.out.print("."); // точки не совпадающие с первой
            }
        }
        System.out.println(currline); // последняя точка для контроля
    }

}
