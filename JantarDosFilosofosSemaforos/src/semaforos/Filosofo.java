package semaforos;

public class Filosofo implements Runnable {

    
    private int idFilosofo;
    
    Mesa mesa;

    public Filosofo(int idFilosofo,  Mesa mesa) {
    	
        this.idFilosofo = idFilosofo;       
        this.mesa = mesa;
        
        new Thread((Runnable)this, "filósofo" + idFilosofo).start();
    }

    @Override
    public void run() {
    	
    	int tempoPensando = 1000;
    	int tempoComendo = 800;
    	 
        while (true) {      
            pensar(tempoPensando);
            pegarTalheres();
            comer(tempoComendo);
            soltarTalheres();
        }
    }

    public void pensar(int tempoPensando) {
        try {
            Thread.sleep(tempoPensando);
        } catch (InterruptedException e) {
            System.out.println("Pensou demais");
        }
    }
    
    public void pegarTalheres() {
        mesa.pegarTalheres(this.idFilosofo);
    }

    public void comer(int tempoComendo) {
        try {
            Thread.sleep(tempoComendo);
        } catch (InterruptedException e) {
            System.out.println("Comeu muito");
        }
    }

    public void soltarTalheres() {
        mesa.soltarTalheres(this.idFilosofo);
    }

}
