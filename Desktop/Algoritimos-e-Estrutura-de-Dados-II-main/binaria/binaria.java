import java.util.*;
import java.io.*;

public class binaria{
    static int comparacoes = 0;
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        String csvFile = "games.csv";
        List<Game> todosGames = new ArrayList<>();
        List<Game> lista = new ArrayList<>();

        
        try(BufferedReader br = new BufferedReader (new FileReader(csvFile))){
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // quebra a linha em varias partes onde tiver , que nao esteja dentro de aspas
                int id = Integer.parseInt(parts[0]); // coloca o id como a primeira parte da string
                String name = parts[1]; // nome do jogo e a segunda parte da string
                todosGames.add(new Game(id, name));// adciona o jogo a lista
            }
        }catch(IOException e){
            System.out.println("Erro ao abrir o arquivo\n");
        }
        while (true) {
            String entrada = sc.nextLine().trim();
            if (entrada.equals("FIM")) break;

            if (entrada.isEmpty()) continue; 

            try {
                int idBusca = Integer.parseInt(entrada);
                for (Game g : todosGames) {
                    if (g.getId() == idBusca) {
                        lista.add(g);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida: " + entrada);
            }
        }
       
       Collections.sort(lista, Comparator.comparing(Game::getName, String.CASE_INSENSITIVE_ORDER).thenComparing(Game::getId));

        long inicioTempo = System.nanoTime();

        while (true) {
            String nomeBusca = sc.nextLine().trim();
            if (nomeBusca.equals("FIM")) break;
            if (nomeBusca.isEmpty()) continue;

            boolean encontrado = buscaBinariaPorNome(lista, nomeBusca);

            if (encontrado) System.out.println("SIM");
            else System.out.println("NAO");
        }

        long fimTempo = System.nanoTime();
        long tempoExecucao = fimTempo - inicioTempo;

            sc.close();

         try {
            String matricula = "869882"; 
            FileWriter log = new FileWriter(matricula + "_binaria.txt"); 
            log.write(matricula + "\t" + tempoExecucao + "\t" + comparacoes); 
            log.close();
        }catch(IOException e){
            System.out.println("Erro ao criar o arquivo de log");
        }
   }   
    

   public static boolean buscaBinariaPorNome(List<Game> lista, String nomeBusca) {
        int inicio = 0;
        int fim = lista.size() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            comparacoes++;

            int comp = lista.get(meio).getName().compareToIgnoreCase(nomeBusca);
            if (comp == 0) return true;
            else if (comp < 0) inicio = meio + 1;
            else fim = meio - 1;
        }
        return false;
    }
}
    

class Game{
    private int id;
    private String name;

    public Game(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}
    public String getName() {return name;}

    public String toString(){
        return id + " ## " + name;
    }
}