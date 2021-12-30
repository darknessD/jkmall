package com.jchen;

import java.lang.annotation.*;

public class TestAnnotation {

    public static void main(String[] args) throws NoSuchFieldException {
        Class<Student> c = Student.class;
        Annotation[] annotations = c.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        Table table = c.getAnnotation(Table.class);
        System.out.println(table.value());
        //Field
        java.lang.reflect.Field field = c.getDeclaredField("name");
        Field annotation = field.getAnnotation(Field.class);
        System.out.println(annotation.column());
        System.out.println(annotation.type());
        System.out.println(annotation.length());
    }
}

@Table(value = "tb_student")
class Student{
    @Field(column = "name", type = "varchar", length = 10)
    private String name;
    @Field(column = "age", type = "int", length = 2)
    private int age;
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Table{
    String value();
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Field{
    String column();
    String type();
    int length();
}