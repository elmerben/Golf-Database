package kanta;

public class tunnusTarkistus {
    
    
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return(int)Math.round(n);
    }
    
    public static double rand2(double d, double e) {
        double n = (e-d)*Math.random() + d;
        return Math.round(n);
    }
    
    
    
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
