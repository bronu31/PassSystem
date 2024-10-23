package org.pass;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcatTest {

    static Concat concat= new Concat("user","admin","00000000");;

    @AfterAll
    static void clean(){
        concat= new Concat("user","admin","00000000");
    }

    @Test
    void addNewContactNull() {
        Concat con1=new Concat(null,null,null);
        Concat con2=new Concat("a",null,null);
        Concat con3=new Concat(null,"b",null);
        Concat con4=new Concat(null,null,"00000001");
        Concat con5=new Concat("a","b",null);
        Concat con6=new Concat("a",null,"00000002");
        Concat con7=new Concat(null,"b","00000002");
        concat.addNewContact(con1);
        concat.addNewContact(con2);
        concat.addNewContact(con3);
        concat.addNewContact(con4);
        concat.addNewContact(con5);
        concat.addNewContact(con6);
        concat.addNewContact(con7);

        assertEquals(new Concat("user","admin","00000000"),concat);
    }
    @Test
    void addNewContactWrong() {
        concat= new Concat("user","admin","00000000");
        Concat con1=new Concat("a","null","78SATFAS");
        Concat con2=new Concat("a","null","78abcdAe");
        concat.addNewContact(con1);
        concat.addNewContact(con2);
        assertEquals(new Concat("user","admin","00000000"),concat);

    }
    @Test
    void addNewContactTrue() {
        Concat con1=new Concat("a","null","00F1C313");
        concat.addNewContact(con1);


        Concat concat1= new Concat("user","admin","00000000");
        concat1.addNewContact(new Concat("a","null","00F1C313"));
        assertEquals(concat1,concat);
    }

    @Test
    void searchEmpty() {
        concat= new Concat("user","admin","00000000");
        assertEquals("Список контактов пуст",concat.search(""));
    }
    @Test
    void searchNull() {
        Concat con1=new Concat("a","null","00F1C313");
        concat.addNewContact(con1);
        assertEquals("Некорректный номер пропуска",concat.search(null));
    }
    @Test
    void searchTrue() {
        concat= new Concat("user","admin","00000000");
        concat.addNewContact(new Concat("a","a","00F1C313"));
        assertEquals("Контакт{name='a', surname='a', passName=00F1C313}",concat.search("00F1C313"));
    }

    @Test
    void deleteEmpty() {
        concat= new Concat("user","admin","00000000");
        assertEquals("Список контактов пуст",concat.delete(""));
    }
    @Test
    void deleteNull() {
        concat= new Concat("user","admin","00000000");
        concat.addNewContact(new Concat("a","a","00F1C313"));
        assertEquals("Некорректный номер пропуска",concat.delete(null));
    }
    @Test
    void deleteNonExistant() {
        concat= new Concat("user","admin","00000000");
        concat.addNewContact(new Concat("a","a","00F1C313"));
        assertEquals("Контакта с таким пропуском не существует",concat.delete("00000005"));
    }
    @Test
    void deleteTrue() {
        concat= new Concat("user","admin","00000000");
        concat.addNewContact(new Concat("a","a","00000005"));
        assertEquals("Удаление совершено успешно",concat.delete("00000005"));
    }


}
