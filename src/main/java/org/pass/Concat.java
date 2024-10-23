package org.pass;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Concat {

    private String name;
    private String surname;
    private String passName;
    private List<Concat> concatList;

    public Concat(String name, String surname, String passName) {
        this.name = name;
        this.surname = surname;
        this.passName = passName;
        this.concatList = new ArrayList<>();
    }


    //Так как список создаётся вместе с созданием объекта нет необходимости проверять что он сузествует
    //Но необходимо сделать валидацию данных
    public boolean addNewContact(Concat concat) {
        //код "00000000" зарезервирован под админ пользователя
        if (validate(concat) && unique(concat.passName)) {
            concatList.add(concat);
            System.out.println("Новый контакт успешно добавлен");
            return true;
        } else {
            System.out.println("Пользователь с данным пропуском уже добавлен");
        }
        return false;
    }

    //проверяем пустой ли список или нет
    //если не пустой то выводим все его элементы последовательно
    public void listAllFull() {
        if (concatList.isEmpty()) {
            System.out.println("Список контактов пуст");
            return;
        }
        for (int i = 0; i < concatList.size(); i++) {
            System.out.println(concatList.get(i));
        }
    }

    //Аналогично предыдущему методу только здесь пишем номер пропуска
    public void listAllPasses() {
        if (concatList.isEmpty()) {
            System.out.println("Список контактов пуст");
            return;
        }
        for (int i = 0; i < concatList.size(); i++) {
            System.out.println(concatList.get(i).passName);
        }
    }

    //необходимо так же проверить что номер переданный нам написан правильно, соответсвенно проводим валидацию перед поиском
    public String search(String pass) {
        if (concatList.isEmpty()) return "Список контактов пуст";
        if (!validate(new Concat("a", "a", pass))) return "Некорректный номер пропуска";
        for (int i = 0; i < concatList.size(); i++) {
            if (concatList.get(i).passName.equals(pass)) {
                return concatList.get(i).toString();
            } else {
                continue;
            }
        }
        return "Контакта с таким пропуском не существует";
    }

    //аналогично предыдущему
    public String delete(String pass) {
        if (concatList.isEmpty()) {
            System.out.println();
            return "Список контактов пуст";
        }
        if (!validate(new Concat("a", "a", pass))) {
            return "Некорректный номер пропуска";
        }
        for (int i = 0; i < concatList.size(); i++) {
            if (concatList.get(i).passName.equals(pass)) {
                concatList.remove(i);
                return "Удаление совершено успешно";
            } else {
                continue;
            }
        }
        return "Контакта с таким пропуском не существует";
    }

    public boolean validate(Concat concat) {
        if ((concat.name == null || concat.surname == null || concat.passName == null)
                || concat.name.isBlank() || concat.surname.isBlank() || concat.passName.isBlank() ||// необходимо проверить что имя и фамилия не пустые и не пробел
                concat.passName.length() != 8 || concat.passName.equals("00000000") || !concat.passName.matches("[0-9A-F]{8}")) {
            //имя пропуска должно быть строго равно 8 и соотвествовать маске
            System.out.println("Введённые данные не корректы");
            return false;
        }


        return true;
    }

    //единственный способ проверитьт уникальность это пройтись по всем контактам и их пропускам
    public boolean unique(String pass) {
        if (concatList.isEmpty()) return true;
        for (int i = 0; i < concatList.size(); i++) {
            if (concatList.get(i).passName.equals(pass)) {

                return false;
            } else {
                continue;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Контакт{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", passName=" + passName +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concat concat = (Concat) o;
        return Objects.equals(name, concat.name) && Objects.equals(surname, concat.surname) && Objects.equals(passName, concat.passName) && Objects.equals(concatList, concat.concatList);
    }

    public Concat readOrCreate(Concat user) {

        File file = new File("save.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        BufferedReader bufferedReader = null;
        try {
            if (file.exists()) {
                bufferedReader = new BufferedReader(new FileReader(file));
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    jsonNode = objectMapper.readTree(s);
                    user.addNewContact(new Concat(
                            jsonNode.get("name").asText(),
                            jsonNode.get("surname").asText(),
                            jsonNode.get("passName").asText())
                    );
                }
                bufferedReader.close();
            } else {
                file.createNewFile();
            }
        } catch (NullPointerException e) {
            System.out.println("Была получена пустая строка, завершаю чтение, файла");
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println("Ошибка при работе с файлом");
                }
            }
        }
        return user;
    }

    public void writeToFile() {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("save.json"));
            bufferedWriter.close();
            bufferedWriter = new BufferedWriter(new FileWriter("save.json", true));
            for (Concat concat : this.concatList) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("name", concat.name);
                objectNode.put("surname", concat.surname);
                objectNode.put("passName", concat.passName);

                bufferedWriter.write(objectNode.toString() + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных");
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println("Ошибка при сохранении данных");
                }
            }
        }
    }

}
