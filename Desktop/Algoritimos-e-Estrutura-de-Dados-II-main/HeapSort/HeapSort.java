import java.io.*;
import java.util.*;


public class HeapSort {

    public static class Game {
        public int id;
        public String nome;
        public String data;
        public String donos;
        public double preco;
        public String idiomas;
        public int notaMetacritic;
        public double notaUsuario;
        public int conquistas;
        public String editora;
        public String desenvolvedora;
        public String categorias;
        public String generos;
        public String tags;

        public Game() {
            id = -1;
            nome = "";
            data = "";
            donos = "";
            preco = -1;
            idiomas = "";
            notaMetacritic = -1;
            notaUsuario = -1;
            conquistas = -1;
            editora = "";
            desenvolvedora = "";
            categorias = "";
            generos = "";
            tags = "";
        }
    }

 public static void main(String[] args) {
        List<Game> todos = carregarCSV("/tmp/games.csv");
        if (todos.isEmpty()) return;

        List<Game> selecionados = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            String entrada = sc.nextLine();
            if (entrada.equals("FIM")) break;
            try {
                int id = Integer.parseInt(entrada);
                for (Game g : todos) {
                    if (g.id == id) {
                        selecionados.add(g);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida: " + entrada);
            }
        }
        sc.close();

        long inicio = System.nanoTime();
        comparacoes = 0;
        movimentacoes = 0;
        heapSort(selecionados);
        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1e9;

        for (Game g : selecionados) imprimirGame(g);

        try (PrintWriter log = new PrintWriter(new FileWriter("869882.txt"))) {
            log.printf("869882\t%d\t%d\t%.6f\n", comparacoes, movimentacoes, tempo);
        } catch (IOException e) {
            System.out.println("Erro ao escrever log: " + e.getMessage());
        }
    }


    static int comparacoes = 0;
    static int movimentacoes = 0;

    
    static String cleanString(String s) {
        if (s == null) return "";
        return s.replace("\"", "").replace("'", "").trim();
    }

    
    static int mesParaNumero(String mes) {
        String[] meses = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (int i = 0; i < meses.length; i++) {
            if (meses[i].equalsIgnoreCase(mes)) return i + 1;
        }
        return 0;
    }

  
    static String formatarData(String orig) {
        try {
            String[] partes = orig.split(" ");
            if (partes.length == 3) {
                int dia = Integer.parseInt(partes[1].replace(",", ""));
                int mes = mesParaNumero(partes[0]);
                String ano = partes[2];
                return String.format("%02d/%02d/%s", dia, mes, ano);
            }
        } catch (Exception e) {
            
        }
        return orig;
    }

   
    static List<Game> carregarCSV(String arquivo) {
        List<Game> lista = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(arquivo));
            String linha = br.readLine(); 
            while ((linha = br.readLine()) != null) {
                String[] campos = splitCSV(linha);
                Game g = new Game();
                g.id = campos[0].isEmpty() ? -1 : Integer.parseInt(campos[0]);
                g.nome = campos[1];
                g.data = formatarData(campos[2]);
                g.donos = campos[3];
                g.preco = campos[4].isEmpty() ? -1 : Double.parseDouble(campos[4]);
                g.idiomas = campos[5];
                g.notaMetacritic = campos[6].isEmpty() ? -1 : Integer.parseInt(campos[6]);
                g.notaUsuario = campos[7].isEmpty() ? -1 : Double.parseDouble(campos[7]);
                g.conquistas = campos[8].isEmpty() ? -1 : Integer.parseInt(campos[8]);
                g.editora = campos[9];
                g.desenvolvedora = campos[10];
                g.categorias = campos[11];
                g.generos = campos[12];
                g.tags = campos[13];
                lista.add(g);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado: " + arquivo);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (IOException e) {}
        }
        return lista;
    }

   
    static String[] splitCSV(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean entreAspas = false;
        for (char c : linha.toCharArray()) {
            if (c == '"') {
                entreAspas = !entreAspas;
            } else if (c == ',' && !entreAspas) {
                campos.add(cleanString(sb.toString()));
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        campos.add(cleanString(sb.toString()));
        while (campos.size() < 14) campos.add(""); 
        return campos.toArray(new String[0]);
    }

    
    static void trocar(List<Game> lista, int i, int j) {
        Game tmp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, tmp);
        movimentacoes++;
    }

    static int donosParaNumero(String donos) {
    if (donos == null || donos.isEmpty()) return -1;
    return Integer.parseInt(donos.replaceAll(",", ""));
}

    
    static void heapify(List<Game> lista, int n, int i) {
    int largest = i;
    int left = 2 * i + 1;
    int right = 2 * i + 2;

    if (left < n) {
        comparacoes++;
        int donosLeft = donosParaNumero(lista.get(left).donos);
        int donosLargest = donosParaNumero(lista.get(largest).donos);
        if (donosLeft > donosLargest || (donosLeft == donosLargest && lista.get(left).id > lista.get(largest).id)) {
            largest = left;
        }
    }

    if (right < n) {
        comparacoes++;
        int donosRight = donosParaNumero(lista.get(right).donos);
        int donosLargest = donosParaNumero(lista.get(largest).donos);
        if (donosRight > donosLargest || (donosRight == donosLargest && lista.get(right).id > lista.get(largest).id)) {
            largest = right;
        }
    }

    if (largest != i) {
        trocar(lista, i, largest);
        heapify(lista, n, largest);
    }
}

   
    static void heapSort(List<Game> lista) {
        int n = lista.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(lista, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            trocar(lista, 0, i);
            heapify(lista, i, 0);
        }
    }

    
    static void imprimirGame(Game g) {
    System.out.printf("=> %d ## %s ## %s ## %s ## %.2f ## %s ## %d ## %.1f ## %d ## [%s] ## [%s] ## [%s] ## [%s] ## [%s] ##\n",
            g.id,
            g.nome,
            g.data,
            g.donos,
            g.preco,
            g.idiomas,
            g.notaMetacritic,
            g.notaUsuario,
            g.conquistas,
            g.editora,
            g.desenvolvedora,
            g.categorias.replace(",", ", "),
            g.generos.replace(",", ", "),
            g.tags.replace(",", ", ")
    );
}


   
}
