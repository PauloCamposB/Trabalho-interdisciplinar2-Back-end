#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>

#define MAX_GAMES 6000
#define MAX_LINE 2048
#define MAX_FIELDS 15
#define MAX_STR 500
#define MAX_LIST 50

typedef struct {
    int id;
    char nome[200];
    char data[50];
    char donos[50];
    double preco;
    char idiomas[MAX_STR];
    int notaMetacritic;
    double notaUsuario;
    int conquistas;
    char editora[200];
    char desenvolvedora[200];
    char categorias[MAX_STR];
    char generos[MAX_STR];
    char tags[MAX_STR];
} Game;


int comparacoes = 0;
int movimentacoes = 0;



void removeChar(char *str, char ch) {
    char *p = str;
    char *q = str;
    while (*p) {
        if (*p != ch) *q++ = *p;
        p++;
    }
    *q = '\0';
}

int parseLista(char *campo, char lista[MAX_LIST][MAX_STR], int tipo) {
    if (!campo || strlen(campo) == 0) {
        strcpy(lista[0], "N/A");
        return 1;
    }
    int j = 0, count = 0;
    char temp[MAX_STR];
    while (*campo) {
        if (*campo == ',' || *campo == '\0') {
            temp[j] = '\0';
            while (*temp == ' ') memmove(temp, temp + 1, strlen(temp));
            if (tipo == 1) { removeChar(temp, '"'); removeChar(temp, '\''); }
            else removeChar(temp, '"');
            strncpy(lista[count], temp, MAX_STR - 1);
            lista[count][MAX_STR - 1] = '\0';
            count++;
            j = 0;
            if (*campo == '\0') break;
        } else {
            temp[j++] = *campo;
        }
        campo++;
    }
    if (j > 0) {
        temp[j] = '\0';
        while (*temp == ' ') memmove(temp, temp + 1, strlen(temp));
        if (tipo == 1) { removeChar(temp, '"'); removeChar(temp, '\''); }
        else removeChar(temp, '"');
        strncpy(lista[count], temp, MAX_STR - 1);
        lista[count][MAX_STR - 1] = '\0';
        count++;
    }
    return count;
}

void showList(char *campo, int colchetes, int isLang) {
    char lista[MAX_LIST][MAX_STR];
    int n = parseLista(campo, lista, isLang);
    if (colchetes) printf("[");
    for (int i = 0; i < n; i++) {
        if (!(n == 1 && strcmp(lista[0], "N/A") == 0)) {
            printf("%s", lista[i]);
            if (i < n - 1) printf(", ");
        }
    }
    if (colchetes) printf("]");
}

int splitCSV(char *linha, char campos[MAX_FIELDS][MAX_STR]) {
    int col = 0, pos = 0, entreAspas = 0;
    for (int i = 0; linha[i]; i++) {
        char ch = linha[i];
        if (ch == '"') entreAspas = !entreAspas;
        else if (ch == ',' && !entreAspas) {
            campos[col][pos] = '\0';
            col++;
            pos = 0;
        } else {
            if (pos < MAX_STR - 1) campos[col][pos++] = ch;
        }
    }
    campos[col][pos] = '\0';
    return col + 1;
}



int mesParaNumero(const char *mes) {
    const char *meses[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    for (int i = 0; i < 12; i++) {
        if (strncasecmp(mes, meses[i], 3) == 0) return i + 1;
    }
    return 0;
}

void formatarData(char *orig, char *dest) {
   
    char mes[20], dia[10], ano[10];
    if (sscanf(orig, "%s %[^,], %s", mes, dia, ano) == 3) {
        int mesNum = mesParaNumero(mes);
        sprintf(dest, "%02d/%02d/%s", atoi(dia), mesNum, ano);
    } else {
    }
}



int carregarCSV(const char *arquivo, Game *lista) {
    FILE *f = fopen(arquivo, "r");
    if (!f) {
        printf("Erro ao abrir %s\n", arquivo);
        return 0;
    }
    char linha[MAX_LINE];
    fgets(linha, sizeof(linha), f); 
    int total = 0;
    while (fgets(linha, sizeof(linha), f)) {
        linha[strcspn(linha, "\r\n")] = '\0';
        char campos[MAX_FIELDS][MAX_STR];
        splitCSV(linha, campos);
        Game g;
        g.id = strlen(campos[0]) > 0 ? atoi(campos[0]) : -1;
        strncpy(g.nome, campos[1], 199);
        formatarData(campos[2], g.data);
        strncpy(g.donos, campos[3], 49);
        g.preco = strlen(campos[4]) > 0 ? atof(campos[4]) : -1.0;
        strncpy(g.idiomas, campos[5], MAX_STR - 1);
        g.notaMetacritic = strlen(campos[6]) > 0 ? atoi(campos[6]) : -1;
        g.notaUsuario = strlen(campos[7]) > 0 ? atof(campos[7]) : -1.0;
        g.conquistas = strlen(campos[8]) > 0 ? atoi(campos[8]) : -1;
        strncpy(g.editora, campos[9], 199);
        strncpy(g.desenvolvedora, campos[10], 199);
        strncpy(g.categorias, campos[11], MAX_STR - 1);
        strncpy(g.generos, campos[12], MAX_STR - 1);
        strncpy(g.tags, campos[13], MAX_STR - 1);
        lista[total++] = g;
        if (total >= MAX_GAMES) break;
    }
    fclose(f);
    return total;
}



int dataParaInt(const char *data) {
    int dia, mes, ano;
    if (sscanf(data, "%d/%d/%d", &dia, &mes, &ano) == 3) {
        return ano * 10000 + mes * 100 + dia;
    }
    return 0;
}


int compararGames(Game *a, Game *b) {
    int dataA = dataParaInt(a->data);
    int dataB = dataParaInt(b->data);

    comparacoes++;
    if (dataA < dataB) return -1;
    else if (dataA > dataB) return 1;
    else {
        comparacoes++;
        if (a->id < b->id) return -1;
        else if (a->id > b->id) return 1;
        else return 0;
    }
}

void trocar(Game *a, Game *b) {
    Game tmp = *a;
    *a = *b;
    *b = tmp;
    movimentacoes++;
}


int particionar(Game *arr, int low, int high) {
    Game pivo = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (compararGames(&arr[j], &pivo) <= 0) {
            i++;
            trocar(&arr[i], &arr[j]);
        }
    }
    trocar(&arr[i + 1], &arr[high]);
    return i + 1;
}

void quickSort(Game *arr, int low, int high) {
    if (low < high) {
        int pi = particionar(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}



void imprimirGame(Game *g) {
double precoTrunc = (int)(g->preco * 10) / 10.0;
printf("=> %d ## %s ## %s ## %s ## %.1f ## ",
       g->id, g->nome, g->data, g->donos, precoTrunc);

    showList(g->idiomas, 0, 1);
    printf(" ## %d ## %.1f ## %d ## ", g->notaMetacritic, g->notaUsuario, g->conquistas);
    showList(g->editora, 1, 0);
    printf(" ## ");
    showList(g->desenvolvedora, 1, 0);
    printf(" ## ");
    showList(g->categorias, 1, 0);
    printf(" ## ");
    showList(g->generos, 1, 0);
    printf(" ## ");
    showList(g->tags, 1, 0);
    printf(" ##\n");
}


int main() {
    Game *todos = malloc(MAX_GAMES * sizeof(Game));
    Game *selecionados = malloc(MAX_GAMES * sizeof(Game));
    int total = carregarCSV("/tmp/games.csv", todos);
    int totalSel = 0;
    char entrada[50];

    while (scanf("%s", entrada) == 1) {
        if (strcmp(entrada, "FIM") == 0) break;
        int id = atoi(entrada);
        for (int i = 0; i < total; i++) {
            if (todos[i].id == id) {
                selecionados[totalSel++] = todos[i];
                break;
            }
        }
    }

clock_t inicio = clock();
comparacoes = 0;
movimentacoes = 0;

quickSort(selecionados, 0, totalSel - 1);

clock_t fim = clock();
double tempo_exec = (double)(fim - inicio) / CLOCKS_PER_SEC;

    for (int i = 0; i < totalSel; i++) imprimirGame(&selecionados[i]);

    FILE *log = fopen("869882.txt", "w");
    if (log) {
        fprintf(log, "869882\t%d\t%d\t%.6lf\n", comparacoes, movimentacoes, tempo_exec);
        fclose(log);
    }

    free(todos);
    free(selecionados);
    return 0;
}
