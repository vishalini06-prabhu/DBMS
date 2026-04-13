import java.util.*;
interface Shape{
   final double pi= 3.14;
   void input();
   void calculateArea();
}
class Circle implements Shape{
   double radius;
   public void input()
   {
      Scanner in = new Scanner(System.in);
      System.out.println("Calculating Area for Shapes");
      System.out.println("Enter the radius:");
      radius =in.nextDouble();
   }
   public void calculateArea()
   {
      System.out.println("Area of Circle:"+pi*radius);
   }
}
class Rectangle implements Shape{
   int l,b;
   public void input()
   {
      Scanner in = new Scanner(System.in);
      System.out.println("Enter the length of Rectangle:");
      l=in.nextInt();
      System.out.println("Enter the breadth of Rectangle:");
      b=in.nextInt();
   }
   public void calculateArea()
   {
      System.out.println("Area of Rectangle:"+l*b);
   }
}
public class ShapePgm{
   public static void main(String[] args)
   {
      Circle c = new Circle();
      c.input();
      c.calculateArea();
      Rectangle r = new Rectangle();
      r.input();
      r.calculateArea();
   }
}
