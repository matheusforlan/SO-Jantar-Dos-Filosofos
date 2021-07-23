package semaforos;

public class Jantar {
    public static void main(String[] args) {
        Mesa mesa = new Mesa();
        
        for (int i = 0; i < Mesa.QUANTIDADE_DE_FILOSOFOS; i++) {
            new Filosofo(i,  mesa);
        }
    }
}
