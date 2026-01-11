import java.util.*;

public class Bubbles_and_Buckets {
    static long inversao = 0;

   static void mergeSort(int[] p, int esq, int dir){
        if(esq < dir){
            int meio = (esq + dir)/2;
            mergeSort(p, esq, meio);
            mergeSort(p, meio + 1, dir);
            merge(p, esq, meio, dir);
        }

    }

    static void merge(int[] p, int esq, int meio, int dir){
        int n1 = meio - esq + 1;   
        int n2 = dir - meio;
        
        int[] a1 = new int[n1 + 1];
        int[] a2 = new int[n2 + 1];

        for(int i = 0; i < n1; i++)
            a1[i] = p[esq + i];

        for(int i = 0; i < n2; i++)
            a2[i] = p[meio + 1 + i];

        a1[n1] = Integer.MAX_VALUE;
        a2[n2] = Integer.MAX_VALUE;

        int i = 0; 
        int j = 0;

        for(int k = esq; k <= dir; k++){
            if(a1[i] <= a2[j]){
                p[k] = a1[i++];
            }else{
                p[k] = a2[j++];
                inversao += (n1 - i); 
            }
        }
    }



    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        
        int n = sc.nextInt(); // 5

        while(n != 0){
            if(n == 0){break;}

            int[] p = new int[n]; 
    
            for(int i = 0;i < n; i++){
                p[i] = sc.nextInt(); // 1 5 3 4 2 
            }
    
    
            mergeSort(p,0,n-1);
    
            
            if(inversao % 2 == 0){
                 System.out.println("Quem escolheu o numero par venceu"); 
            
            }else{ 
                System.out.println("Quem escolheu o numero impar venceu"); 
            }

            inversao = 0;
            n = sc.nextInt();

        }
        sc.close();
    }    
}
