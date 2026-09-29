# Racas RPG (NeoForge 1.21.1) - versao 0.1.0

Mod de racas com evolucao por missoes.

## Como jogar
- `/raca info`      lista as racas, estagios, bonus e missoes
- `/raca escolher <humano|elfo|anao|orc>`   escolhe a raca (so uma vez)
- `/raca`           mostra seu estagio, bonus e progresso da missao
- `/raca evoluir`   evolui quando a missao do estagio estiver completa
- `/raca resetar`   (so OP/cheats) remove a raca, util para testar

Missoes por raca: Humano = abater monstros; Elfo = abater monstros a distancia (arco/besta);
Anao = minerar minerios; Orc = abater monstros no corpo a corpo.
Cada raca tem 3 estagios com bonus/penalidades de atributos diferentes.

## Como compilar (voce precisa fazer isso uma vez)
1. Instale o **JDK 21** (por ex. Temurin 21).
2. Abra um terminal nesta pasta e rode:
   - Windows: `gradlew.bat build`
   - Linux/Mac: `./gradlew build`
   (a primeira vez baixa varias coisas e demora alguns minutos; precisa de internet)
3. O mod fica em `build/libs/racasrpg-0.1.0.jar`. Coloque na pasta `mods` do Minecraft 1.21.1 com NeoForge.

## Testar direto do projeto (sem instalar)
`gradlew runClient` abre o jogo ja com o mod.

Se der erro ao compilar ou ao abrir o jogo, mande o texto completo do erro.
