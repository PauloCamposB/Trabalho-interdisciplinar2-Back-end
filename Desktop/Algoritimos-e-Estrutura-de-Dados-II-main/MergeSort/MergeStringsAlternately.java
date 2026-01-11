/*   Você recebe duas strings, ` word1a` e `b` word2. Combine as strings adicionando letras em ordem alternada, começando com `a` word1. Se uma string for mais longa que a outra, anexe as letras adicionais ao final da string resultante.

Retorne a string mesclada.

 

Exemplo 1:

Entrada: palavra1 = "abc", palavra2 = "pqr"
 Saída: "apbqcr"
 Explicação:  A string resultante será mesclada da seguinte forma:
palavra1: abc
palavra2: pqr
mesclado: apbqcr
Exemplo 2:

Entrada: palavra1 = "ab", palavra2 = "pqrs"
 Saída: "apbqrs"
 Explicação:  Observe que, como a palavra2 é mais longa, "rs" é adicionado ao final.
palavra1: ab
palavra2: pqrs
mesclado: apbqrs
Exemplo 3:

Entrada: palavra1 = "abcd", palavra2 = "pq"
 Saída: "apbqcd"
 Explicação:  Observe que, como a palavra1 é mais longa, "cd" é adicionado ao final.
palavra1: abcd
palavra2: pq
mesclado: apbqcd
 

Restrições:

1 <= word1.length, word2.length <= 100
word1e word2são compostas por letras minúsculas do alfabeto inglês.*/

import java.util.*;

class MergeStringsAlternately {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String word1 = sc.nextLine();
        String word2 = sc.nextLine();

        StringBuilder word3 = new StringBuilder();

        int max = Math.max(word1.length(), word2.length());

        for (int i = 0; i < max; i++) {
            if (i < word1.length()) {
                word3.append(word1.charAt(i));
            }
            if (i < word2.length()) {
                word3.append(word2.charAt(i));
            }
        }

        System.out.println(word3.toString());
        sc.close();
    }
}
