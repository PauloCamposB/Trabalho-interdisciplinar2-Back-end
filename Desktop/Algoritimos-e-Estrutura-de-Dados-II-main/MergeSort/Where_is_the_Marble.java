import java.util.*;

public class Where_is_the_Marble {

    // ===== MERGE SORT =====
    static void mergeSort(int[] v, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSort(v, esq, meio);
            mergeSort(v, meio + 1, dir);
            merge(v, esq, meio, dir);
        }
    }

    static void merge(int[] v, int esq, int meio, int dir) {
        int n1 = meio - esq + 1;
        int n2 = dir - meio;

        int[] a1 = new int[n1 + 1];
        int[] a2 = new int[n2 + 1];

        for (int i = 0; i < n1; i++)
            a1[i] = v[esq + i];

        for (int j = 0; j < n2; j++)
            a2[j] = v[meio + 1 + j];

        a1[n1] = a2[n2] = Integer.MAX_VALUE;

        int i = 0, j = 0;
        for (int k = esq; k <= dir; k++) {
            if (a1[i] <= a2[j])
                v[k] = a1[i++];
            else
                v[k] = a2[j++];
        }
    }

    // ===== BUSCA BINÁRIA (PRIMEIRA OCORRÊNCIA) =====
    static int buscaPrimeira(int[] v, int x) {
        int inicio = 0, fim = v.length - 1;
        int resp = -1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;

            if (v[meio] == x) {
                resp = meio;
                fim = meio - 1; // continua procurando à esquerda
            } else if (v[meio] < x) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return resp;
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int caso = 1;

        while (true) {
            int n = sc.nextInt();
            int q = sc.nextInt();

            if (n == 0 && q == 0)
                break;

            int[] array = new int[n];
            for (int i = 0; i < n; i++)
                array[i] = sc.nextInt();

            mergeSort(array, 0, n - 1);

            System.out.println("CASE# " + caso + ":");

            for (int i = 0; i < q; i++) {
                int x = sc.nextInt();
                int pos = buscaPrimeira(array, x);

                if (pos != -1)
                    System.out.println(x + " found at " + (pos + 1));
                else
                    System.out.println(x + " not found");
            }

            caso++;
        }

        sc.close();
    }
}
