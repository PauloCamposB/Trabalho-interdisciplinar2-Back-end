import java.io.*;
import java.util.*;

public class MergeSortGames {

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
            id = -1; nome = ""; data = ""; donos = ""; preco = -1; idiomas = "";
            notaMetacritic = -1; notaUsuario = -1; conquistas = -1;
            editora = ""; desenvolvedora = ""; categorias = ""; generos = ""; tags = "";
        }
    }

    static int comparacoes = 0;
    static int movimentacoes = 0;
    static PrintStream out; 

    public static void main(String[] args) throws Exception {
        out = new PrintStream(System.out, true, "UTF-8"); 
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
                out.println("Entrada invalida: " + entrada);
            }
        }
        sc.close();

        long inicio = System.nanoTime();
        comparacoes = 0;
        movimentacoes = 0;
        mergeSort(selecionados, 0, selecionados.size() - 1);
        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1e9;

        imprimirTopAndBottom(selecionados);

        try (PrintWriter log = new PrintWriter(new FileWriter("869882.txt"))) {
            log.printf("869882\t%d\t%d\t%.6f\n", comparacoes, movimentacoes, tempo);
        } catch (IOException e) {
            out.println("Erro ao escrever log: " + e.getMessage());
        }
    }

  static void imprimirTopAndBottom(List<Game> lista) {
    int n = lista.size();

    out.println("| 5 precos mais caros |");
    
    for (int i = n - 1; i >= Math.max(n - 5, 0); i--) {
        imprimirGame(lista.get(i));
    }

    out.println("| 5 precos mais baratos |");
   
    for (int i = 0; i < Math.min(5, n); i++) {
        imprimirGame(lista.get(i));
    }
}


static void imprimirGame(Game g) {
    out.printf("=> %d ## %s ## %s ## %s ## %s ## %s ## %d ## %s ## %d ## [%s] ## [%s] ## [%s] ## [%s] ## [%s] ##\n",
            g.id,
            g.nome,
            g.data,
            g.donos,
            formatarDecimal(g.preco),
            g.idiomas,
            g.notaMetacritic,
            formatarDecimal(g.notaUsuario),
            g.conquistas,
            g.editora,
            g.desenvolvedora.replace(",", ", "),
            g.categorias.replace(",", ", "),
            g.generos.replace(",", ", "),
            g.tags.replace(",", ", ")
    );
}


static String formatarDecimal(double valor) {
    if (valor < 0) return "0.0"; 
    valor = Math.floor(valor * 10) / 10.0; 
    String s = String.valueOf(valor);
    if (!s.contains(".")) s += ".0";
    return s;
}




    static void mergeSort(List<Game> lista, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(lista, left, mid);
            mergeSort(lista, mid + 1, right);
            merge(lista, left, mid, right);
        }
    }

    static void merge(List<Game> lista, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Game> L = new ArrayList<>();
        List<Game> R = new ArrayList<>();
        for (int i = 0; i < n1; i++) L.add(lista.get(left + i));
        for (int i = 0; i < n2; i++) R.add(lista.get(mid + 1 + i));

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            comparacoes++;
            Game gL = L.get(i);
            Game gR = R.get(j);
            if (gL.preco < gR.preco || (gL.preco == gR.preco && gL.id < gR.id)) {
                lista.set(k++, gL);
                movimentacoes++;
                i++;
            } else {
                lista.set(k++, gR);
                movimentacoes++;
                j++;
            }
        }
        while (i < n1) { lista.set(k++, L.get(i++)); movimentacoes++; }
        while (j < n2) { lista.set(k++, R.get(j++)); movimentacoes++; }
    }

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
        } catch (Exception e) {}
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
            if (c == '"') entreAspas = !entreAspas;
            else if (c == ',' && !entreAspas) {
                campos.add(cleanString(sb.toString()));
                sb.setLength(0);
            } else sb.append(c);
        }
        campos.add(cleanString(sb.toString()));
        while (campos.size() < 14) campos.add("");
        return campos.toArray(new String[0]);
    }

    
}
