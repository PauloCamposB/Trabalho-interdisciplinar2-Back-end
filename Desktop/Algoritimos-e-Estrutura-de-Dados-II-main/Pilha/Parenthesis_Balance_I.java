package pilha;

public class Parenthesis_Balance_I {
    
    static class celula{
        public int elemento; 
        public celula prox;
    
        public celula(){
            this(0);
        }
    
        public celula(int elemento){
            this.elemento = elemento;
            this.prox = null;
           }
    }
    
    static class pilha{
        private celula topo;
        
        public pilha(){
            topo = null;
        }

        public void empilhar(int elemento){
            celula tmp = new celula(elemento);
            tmp.prox = topo;
            topo =  tmp;
            tmp = null;
        }


        public boolean desempilhar(){
            if(topo == null){
                return true;
            }
            //int resp = topo.elemento;
            celula tmp = topo;
            topo = topo.prox;
            tmp.prox = null;
            tmp = null;
            return false;
        }

        public boolean vazia() {
            return topo == null;
        }

    }
    public static void main(String[] args){
        pilha p = new pilha();
        boolean erro = false;

        String array = "(a+((b*c)-)2-a";

        for(int i = 0; i < array.length(); i++){
            if(array.charAt(i) == '(' ){
                p.empilhar(1);
            }
            if(array.charAt(i) == ')'){
                if(p.desempilhar()){
                    erro = true;
                    break;
                }
            }
        }
   
        if (!erro && !p.vazia()) {
            erro = true;
        }

        if(erro){
         System.out.print("Erro ao fechar os parenteses");
        }else{
            System.out.print("parenteses fechado com sucesso");
        }
        
        

    }
}
