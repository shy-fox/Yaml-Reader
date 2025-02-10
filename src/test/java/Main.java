import io.shiromi.saml.types.NumberType;

public class Main {
    public static void main(String[] args) {
        System.out.println(NumberType.parseNumber("0o277", 8));
    }
}