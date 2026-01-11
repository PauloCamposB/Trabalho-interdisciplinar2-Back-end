import java.util.*;


public class Sort_Sort_And_Sort {



    static void mergeSort(int[] p, int esq, int dir, int m){
        if(esq < dir){
            int meio = (esq + dir)/2;
            mergeSort(p, esq, meio, m);
            mergeSort(p, meio + 1, dir, m);
            merge(p,esq,meio,dir, m);
        }


    }

    static void merge(int[] p, int esq, int meio,int dir, int m){
            int n1 = meio - esq + 1;
            int n2 = dir - meio;

            int[] a1 = new int[n1 + 1];
            int[] a2 = new int[n2 + 1];

            
        for(int i = 0; i < n1; i++)
            a1[i] = p[esq + i];

        for(int i = 0; i < n2; i++)
            a2[i] = p[meio + 1 + i];



        int i = 0; 
        int j = 0;
        int k = esq;

        while(i < n1 && j < n2){
            int x = a1[i];
            int y = a2[j];

            if((x % m) < (y % m)){
                p[k++] = a1[i++];
            }else  if((x % m) > (y % m)){
                p[k++] = a2[j++];

            }else {
                if (x % 2 != 0 && y % 2 == 0) {
                
                    p[k++] = a1[i++];
                }
                else if (x % 2 == 0 && y % 2 != 0) {
                    p[k++] = a2[j++];
                }
                else if (x % 2 != 0) {
                
                    if (x >= y) p[k++] = a1[i++];
                    else        p[k++] = a2[j++];
                }
                else {
                    
                    if (x <= y) p[k++] = a1[i++];
                    else        p[k++] = a2[j++];
                }
}

        }

        while (i<n1){
            p[k++] =a1[i++];
        }
        while(j < n2){
            p[k++]= a2[j++]; 
       }
    


                
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
    
        
        int n = 10;
        int[] array = {20,36,11,7,21,10,8,15,14,9};
        int m = 5;

    
        System.out.println("Array: " + Arrays.toString(array));
        
        mergeSort(array, 0, n-1, m);

        System.out.println("Array ordenado: " + Arrays.toString(array));

        sc.close();
    }
}
