import controller.TestApi;

public class Main {
    public static void main(String[] args) {
        TestApi apiObj = new TestApi();
        String result = apiObj.helloTestApi("Наказнюк Егор");
        System.out.println(result);
    }
}