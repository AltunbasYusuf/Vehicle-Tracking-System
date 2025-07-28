//import domain.vehicle.EmissionCalculator;
//import domain.vehicle.Vehicle;
//import presentation.setup.Quick_Setup;
//
//import java.util.Scanner;
//public class TestClass {
//    public static void main(String[] args) {
//        Scanner input =new Scanner(System.in);
//        Quick_Setup qs =new Quick_Setup();
//        Vehicle vehicle=qs.quickSetup();
//        EmissionCalculator co2calculate= new EmissionCalculator();
//        String fuelType= vehicle.getFuelType();
//        double emmisionFactor=co2calculate.emmisionCalculating(fuelType);
//        System.out.println("Give km for co2 test(If you want to exit please press 0)");
//        double km=input.nextDouble();
//        double co2=0;
//        while (km!=0){
//             co2= co2calculate.calculatingCO2(km,emmisionFactor)+co2;
//            System.out.println(co2);
//            km=input.nextDouble();
//        }
//    }
//}
