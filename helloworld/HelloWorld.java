
public class HelloWorld {

    public static String getMsg() {
        HW hw = new HW();
        hw.gen();
        return hw.getMassage();

    }

    public static void main(String[] args){
        HW hw = new HW();
        hw.gen();
        System.out.println(hw.getMassage());
    }
}