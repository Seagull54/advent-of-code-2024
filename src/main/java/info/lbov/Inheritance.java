package info.lbov;

public class Inheritance {

    public static void main(String[] args) {
        Child child = new Child();
        child.print();
    }
}
class Parent {

    Parent() {
        System.out.println("parent");
    }

    public void print() {
        System.out.println("parent");
    }
}

class Child extends Parent {
    Child() {
        System.out.println("im child");
    }

    public void print() {
        System.out.println("im child");
        super.print();
    }
}


