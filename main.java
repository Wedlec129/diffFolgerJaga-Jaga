//javac main.java && java main
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class main {

    private static JTextArea leftTextArea; // Панель для вывода левой директории
    private static JTextArea rightTextArea; // Панель для вывода правой директории


    public static void main(String[] args) {
        JFrame frame = new JFrame("Folder jaga-jaga"); // Создаем главное окно
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Устанавливаем операцию закрытия приложения
        frame.setSize(800, 600); // Устанавливаем размер окна

        //Пример
        String path1 = "passwords/1";
	String path2 = "passwords/2";
        


        // Панель для размещения текстовых областей и кнопки
        JPanel panel = new JPanel(new GridLayout(1, 2));

        leftTextArea = new JTextArea(30, 20); // Создаем текстовую область для левой директории
        leftTextArea.setBackground(new Color(183, 219, 219)); // Устанавливаем цвет фона
        leftTextArea.setText(path1);

        rightTextArea = new JTextArea(30, 20); // Создаем текстовую область для правой директории
        rightTextArea.setBackground(new Color(255, 192, 203)); // Устанавливаем цвет фона
        rightTextArea.setText(path2);

        // Создаем кнопку для сравнения директорий
        JButton compareButton = new JButton("Jaga-Jaga");
        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path1 = leftTextArea.getText();
                String path2 = rightTextArea.getText();
                compareFolders(path1, path2); // Вызываем метод сравнения директорий
            }
        });


        panel.add(leftTextArea);
        panel.add(rightTextArea);


        frame.add(panel, BorderLayout.CENTER);
        frame.add(compareButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Метод сравнения двух папок
    public static void compareFolders(String path1, String path2) {
        File folder1 = new File(path1); // Первая папка
        File folder2 = new File(path2); // Вторая папка

        // Проверяем, что обе папки существуют
        if (folder1.isDirectory() && folder2.isDirectory()) {
            StringBuilder leftBuilder = new StringBuilder();
            StringBuilder rightBuilder = new StringBuilder();

            // Обходим все файлы первой папки
            for (File item1 : folder1.listFiles()) {
                //пропускаем все скрытые папки
                if (item1.getName().startsWith(".")) {
                    continue;
                }
                boolean found = false;
                // Ищем файлы с тем же именем во второй папке
                for (File item2 : folder2.listFiles()) {
                    //пропускаем все скрытые папки
                    if (item2.getName().startsWith(".")) {
                        continue;
                    }
                    // Если файлы совпадают по именам
                    if (item1.getName().equals(item2.getName())) {
                        found = true;
                        // Если оба файла не являются директориями
                        if (!item1.isDirectory() && !item2.isDirectory()) {
                            // Сравниваем содержимое файлов
                            try (FileInputStream fis1 = new FileInputStream(item1);
                                 FileInputStream fis2 = new FileInputStream(item2)) {
                                byte[] file1Bytes = fis1.readAllBytes();
                                byte[] file2Bytes = fis2.readAllBytes();
                                if (Arrays.equals(file1Bytes, file2Bytes)) {
                                    leftBuilder.append(item1.getName()).append("\n");
                                    rightBuilder.append(item2.getName()).append("\n");
                                } else {
                                    leftBuilder.append(getFormattedText(item1, item2, "yellow")).append("\n");
                                    rightBuilder.append(getFormattedText(item2, item1, "yellow")).append("\n");
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            leftBuilder.append(getFormattedText(item1, item2, "yellow")).append("\n");
                            rightBuilder.append(getFormattedText(item2, item1, "yellow")).append("\n");
                        }
                    }
                }
                // Если файл не был найден во второй папке
                if (!found) {
                    rightBuilder.append(getFormattedText(item1, null, "red")).append("\n");
                    leftBuilder.append(getFormattedText(item1, null, "green")).append("\n");
                }
            }

            // Обходим все файлы второй папки
            for (File item2 : folder2.listFiles()) {
                //пропускаем все скрытые папки
                if (item2.getName().startsWith(".")) {
                    continue;
                }
                boolean found = false;
                // Ищем файлы с тем же именем в первой папке
                for (File item1 : folder1.listFiles()) {
                    //пропускаем все скрытые папки
                    if (item1.getName().startsWith(".")) {
                        continue;
                    }
                    // Если файл найден
                    if (item2.getName().equals(item1.getName())) {
                        found = true;
                    }
                }
                // Если файл не был найден в первой папке
                if (!found) {
                    rightBuilder.append(getFormattedText(item2, null, "red")).append("\n");
                    leftBuilder.append(getFormattedText(item2, null, "green")).append("\n");
                }
            }

            leftTextArea.setText(leftBuilder.toString()); // Устанавливаем текст левой директории
            rightTextArea.setText(rightBuilder.toString()); // Устанавливаем текст правой директории
        } else {
            //ответ поумолчанию
            leftTextArea.setText("введите путь до папки 1");
            rightTextArea.setText("введите путь до папки 2");
        }
    }

    // Метод для форматирования текста в зависимости от цвета
    private static String getFormattedText(File item, File itemToCompare, String color) {
        String ansver = "";
        switch (color) {
            case "yellow":
                ansver = "Yellow " + item.getName() + " файлы ОТЛИЧАЮТСЯ";
                break;
            case "red":
                ansver = "Red " + item.getName() + " файл ОТСУТВУЕТ";
                break;
            case "green":
                ansver = "Green " + item.getName() + " файл ПРИСУТВУЕТ";
                break;
        }
        return ansver;
    }
}