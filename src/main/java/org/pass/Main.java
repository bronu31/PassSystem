package org.pass;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Concat user = new Concat("user", "admin", "00000000");
        Scanner in = new Scanner(System.in);
        System.out.println("Добро пожаловать в систему управления пропусками");
        user = user.readOrCreate(user);
        loop:
        while (true) {
            System.out.println("Введите 1 для добавления контакта");
            System.out.println("Введите 2 для просмотра контаков");
            System.out.println("Введите 3 для поиска контакта по номеру пропуска");
            System.out.println("Введите 4 для удаления контакта (необзодимо ввести номер пропуска)");
            System.out.println("Введите 5 для выхода из программы" + "\n");
            try {
                int choise = in.nextInt();
                switch (choise) {
                    case 1:

                        System.out.println("Введите имя, фамилию, номер пропуска");
                        in.nextLine();
                        String name = in.nextLine();
                        String surname = in.nextLine();
                        String passName = in.nextLine();
                        break;
                    case 2:
                        System.out.println("Введите 1 отображения полной информации по контактам");
                        System.out.println("Введите 2 отображения пропусков");
                        choise = in.nextInt();
                        switch (choise) {
                            case 1:
                                user.listAllFull();
                                break;
                            case 2:
                                user.listAllPasses();
                                break;
                            default:
                                System.out.println("Возврат к выбору операций");
                        }
                        break;
                    case 3:
                        System.out.println("Введите номер пропуска");
                        in.nextLine();
                        String passname = in.nextLine();
                        System.out.println(user.search(passname));
                        break;
                    case 4:
                        System.out.println("Введите номер пропуска");
                        in.nextLine();
                        String pass = in.nextLine();
                        user.delete(pass);
                        break;
                    case 5:
                        System.out.println("Завершение работы");
                        user.writeToFile();
                        break loop;
                    default:
                        System.out.println("Попытка совершить несуществующее действие, пожалуйста введите корректный номер действия");
                }

            } catch (InputMismatchException e) {
                System.out.println("Введён некорректный символ, введите корректный номер операции");
                System.out.println();
                in.next();
            } catch (Exception e) {
                System.out.println("Ошибка при выполнении операции, пожалуйста проверьте корректность данных и попробуйте снова");
            }
        }
        in.close();
    }
}
