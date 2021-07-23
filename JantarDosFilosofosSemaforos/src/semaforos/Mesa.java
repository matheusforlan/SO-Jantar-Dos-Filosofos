package semaforos;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Mesa {
	
	// se quiser mudar a quantidade de filósofos, é só mudar esta variável.
	final static int QUANTIDADE_DE_FILOSOFOS = 5;

    Semaphore mutex;
    
    Semaphore[] talheres = new Semaphore[QUANTIDADE_DE_FILOSOFOS];
    
    Semaphore[] filosofos = new Semaphore[QUANTIDADE_DE_FILOSOFOS];
    
    EstadoFilosofo[] estadoDosFilosofos = new EstadoFilosofo[QUANTIDADE_DE_FILOSOFOS];
   

    public Mesa() {
    	
        for (int i = 0; i < QUANTIDADE_DE_FILOSOFOS; i++) {
            talheres[i] = new Semaphore(1);
            estadoDosFilosofos[i] = EstadoFilosofo.PENSANDO;
            filosofos[i] = new Semaphore(0);
        }
        
        this.mutex = new Semaphore(1);
        
        imprimeEstadosDosFilosofos();
    }
    

    private int filosofoDaEsquerda(int indice_filosofo) {
        int filosofoDaEsquerda;
        if (indice_filosofo == 0) {
            filosofoDaEsquerda = QUANTIDADE_DE_FILOSOFOS - 1;
        } else {
            filosofoDaEsquerda = indice_filosofo - 1;
        }
        return filosofoDaEsquerda;
    }

    private int filosofoDaDireita(int indice_filosofo) {
        int filosofoDaDireita;
        if (indice_filosofo == QUANTIDADE_DE_FILOSOFOS - 1) {
            filosofoDaDireita = 0;
        } else {
            filosofoDaDireita = indice_filosofo + 1;
        }
        return filosofoDaDireita;
    }
    
    private boolean filosofoPodeComer(int indice_filosofo) {
        return estadoDosFilosofos[filosofoDaDireita(indice_filosofo)] != EstadoFilosofo.COMENDO
                && estadoDosFilosofos[filosofoDaEsquerda(indice_filosofo)] != EstadoFilosofo.COMENDO;
    }
    
    private boolean filosofoDaEsquerdaPodeComer(int indice_filosofo) {
        return estadoDosFilosofos[filosofoDaEsquerda(indice_filosofo)] == EstadoFilosofo.FAMINTO
                && estadoDosFilosofos[filosofoDaEsquerda(filosofoDaEsquerda(indice_filosofo))] != EstadoFilosofo.COMENDO;
    }
    
    private boolean filosofoDaDireitaPodeComer(int indice_filosofo) {
        return estadoDosFilosofos[filosofoDaDireita(indice_filosofo)] == EstadoFilosofo.FAMINTO
                && estadoDosFilosofos[filosofoDaDireita(filosofoDaDireita(indice_filosofo))] != EstadoFilosofo.COMENDO;
    }


    public void soltarTalheres(int indice_filosofo) {
    	
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
       
        imprimeEstadosDosFilosofos();
        estadoDosFilosofos[indice_filosofo] = EstadoFilosofo.PENSANDO;
        
        if (filosofoDaDireitaPodeComer(indice_filosofo)) {
            estadoDosFilosofos[filosofoDaDireita(indice_filosofo)] = EstadoFilosofo.COMENDO;
            filosofos[filosofoDaDireita(indice_filosofo)].release();
        }

        if (filosofoDaEsquerdaPodeComer(indice_filosofo)) {
            estadoDosFilosofos[filosofoDaEsquerda(indice_filosofo)] = EstadoFilosofo.COMENDO;
            filosofos[filosofoDaEsquerda(indice_filosofo)].release();
        }

        mutex.release();
    }
    
    public void pegarTalheres(int indice_filosofo) {
    	
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        estadoDosFilosofos[indice_filosofo] = EstadoFilosofo.FAMINTO;
        
        if (filosofoPodeComer(indice_filosofo)) {
            filosofos[indice_filosofo].release();
            estadoDosFilosofos[indice_filosofo] = EstadoFilosofo.COMENDO;
        }
        
        mutex.release();
        
        try {
            filosofos[indice_filosofo].acquire();
            System.out.println(Arrays.toString(estadoDosFilosofos));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void imprimeEstadosDosFilosofos() {
    	System.out.println(Arrays.toString(estadoDosFilosofos));
    }
    
}
