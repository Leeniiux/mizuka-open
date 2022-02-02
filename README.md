# Confidentialité, intégrité et authentification de données

L'objectif de ce TD est de vous faire manipuler quelques outils de linux pour calculer et vérifier la confidentialité, l'intégrité et l'authentification de données, en vous rappelant dans un premier temps les principes théoriques sous-jacents à ces services.

Pour répondre aux questions, utilisez vos supports de cours ainsi que les pointeurs web communiqués.

---
## Exercice 1: Chiffrement symétrique et asymétrique (20-30 minutes)

Un groupe de `n` personnes souhaite utiliser un système cryptographique pour s'échanger deux à deux des informations confidentielles. Les informations échangées entre deux membres du groupe ne devront pas pouvoir être lues par un autre membre.

Le groupe décide d'utiliser un _système symétrique de chiffrement_.

- Quel est le nombre minimal de clefs symétriques nécessaires ?

- Donner le nom d'un algorithme de chiffrement symétrique reconnu.

Le groupe décide ensuite de remplacer ce système par un _système asymétrique_.

- Quel est le nombre minimal de couples de clés asymétriques nécessaires pour que chaque membre puisse envoyer et recevoir des informations chiffrées et/ou signées ?

- Alice souhaite envoyer des informations chiffrées et signées à Bob (Alice et Bob appartiennent tous les deux au groupe). Quelle(s) clef(s) Bob doit-il utiliser ?

- Donner le nom d'un algorithme de chiffrement asymétrique reconnu.

Le groupe décide finalement d'utiliser un système hybride pour le chiffrement (i.e. qui utilise à la fois le cryptographie symétrique et asymétrique)

- Donner les raisons qui ont poussées le groupe à utiliser un tel système.

---
## Exercice 2 : Certificats (15 minutes)

Discuter les trois scénarii suivants en termes de sécurité :

- Deux certificats différents sont signés par la même clé privée

- Deux certificats différents contiennent la même clé publique

- Deux certificats différents ont la même empreinte



---
## Exercice 3 : Droits d'utilisation des moyens de cryptologie en France (20-30 minutes)

En 1999, la loi française autorisait l'utilisation de "_matériels ou logiciels offrant un service de confidentialité mis en œuvre par un algorithme dont la clef [...avec...] une longueur inférieure ou égale à 128 bits_" [1].

Le texte a été abrogé en avril 2007 et la loi pour la confiance dans l'économie numérique (LCEN) de 2004 [2;3] vient clarifier le cadre actuel.

- Ainsi aujourd'hui, sommes-nous libre d'utiliser les moyens de cryptologie disponibles ? Y a-t-il une limite dans la longueur de clé maximale utilisable ? Une limite minimale conseillée ?

Pour répondre à la question, jeter sur un document de l'ANSSI à l'attention des particuliers qui a le mérite de fournir une contextualisation historique [4]. Vous pouvez chercher aussi autour du 1er item de l'article 30 du texte de loi LCEN [2].

Vous pouvez aussi jeter un oeil aux recommandations concernant les tailles de clé ici [5].

[1] https://www.legifrance.gouv.fr/affichTexte.do?cidTexte=LEGITEXT000005627660

[2] https://www.legifrance.gouv.fr/affichTexte.do?cidTexte=JORFTEXT000000801164

[3] https://fr.wikipedia.org/wiki/Loi_pour_la_confiance_dans_l%27%C3%A9conomie_num%C3%A9rique 

[4] https://www.ssi.gouv.fr/particulier/bonnes-pratiques/crypto-le-webdoc/securite-secret-et-legalite/

[5] https://www.keylength.com/fr/5/

---
## Exercice 4 : Chiffrer, déchiffrer, signer avec PGP/GPG (45 minutes)

### Pretty quoi ? 

**PGP (_Pretty Good Privacy_)** est un logiciel de chiffrement cryptographique, développé et diffusé aux États-Unis par Philip Zimmermann en 1991. PGP se propose de garantir la confidentialité et l'authentification pour la communication des données en combinant des mécanismes de cryptographie asymétrique et de cryptographie symétrique.

- Lire les paragraphes sur l'_authentification_ et la _confidentialité_ de la section _Fonctionnement_ de https://fr.wikipedia.org/wiki/Pretty_Good_Privacy.

Zimmermann a pensé et diffusé librement PGP hors des états-unis (en dépit de la législation du moment) dans l'idée de "_donner aux gens le pouvoir de prendre en main leur intimité._"

PGP trouve un usage dans les échanges par courriers électroniques. PGP suit le standard OpenPGP (RFC 4880) défini par l'Internet Engineering Task Force (IETF).

- Qu'est GPG par rapport à GPG ? Lire https://gnupg.org

- Peut on utiliser GPG en France ?

### Génération d'une paire de clés privée/publique avec `gpg`

L'environnement Linux dispose de la commande `gpg` et du `man`(uel) correspondant. 

- Générer une paire de clé privée/publique (opter pour les choix par défaut). 

Ce sera une de vos paires de clés. Attention n'attendez pas que les clefs se génèrent comme cela. Ce processus requiert de l'aléas. Ouvrir par exemple un terminal et faire un copier-coller d'une dizaine de fois la même page wikipedia. Et si ca vous ne voyez toujours rien sortir du terminal où la commande de génération a été lancée alors répéter autant que nécessaire les copier-coller avec un autre terminal !!

    gpg --gen-key

Avec `gpg`, les clés privées et publiques de votre trousseau de clés sont présentes dans le répertoire `~/.gnupg`. 

- Vérifier les clés publiques présentes (déjà importées) dans votre trousseau de clés.

    gpg --list-keys

L'identifiant `IDPUBLICKEY` de chaque clé publique se trouve sur chaque ligne préfixée par `pub` juste après le slash qui suit la taille de clé (en général `1024D` ou `2048g`). 

- Quel est l'identifiant `IDPUBLICKEY` de votre clé publique ?

### Diffusion de votre clé publique

Votre clé publique va permettre à vos interlocuteurs de vous envoyer des messages confidentiels. Mais pour cela il faut la rendre publique...

- Exporter votre clé publique en `ascii armored`. Donnez lui le nom `VOTRENOM_VOTREPRENOM.pub` et en choisissant votre nom et prénom à la place de `VOTRENOM_VOTREPRENOM`.

Si vous avez plusieurs paires de clé privé/publique il vous faut identifier celle que vous souhaitez exporter. Pour cela indiquez en argument de `--export` soit son identifiant `IDPUBLICKEY` soit un morceau de celui-ci comme une adresse mail. Ici indiquez l'identifiant `IDPUBLICKEY` que vous avez relevé pour votre clé publique. 

    gpg --list-keys
    gpg --armor --export IDPUBLICKEY > VOTRENOM_VOTREPRENOM.pub

- A quoi correspond l'option `--armor` et que signifie `ascii armored` ? Le manuel et ce lien stackexchange peuvent vous être utiles 
https://unix.stackexchange.com/questions/623375/what-is-the-armored-option-for-in-gnupg.

- Diffuser votre clé publique pour que vos collègues puissent l'utiliser pour vous envoyer des messages confidentiels. 

D'un point de vue pratique, pour rendre disponible votre clé publique, vous êtes libre d'utiliser le mail, une clé usb, ou mettre en oeuvre vos compétences réseaux en configurant vos machines sur un même réseau IP et en déployant vos fichiers sur /var/www/html (accessible via http://adresse.ip.qui.va.bien/). Prenez tout de même quelques secondes pour vous interroger sur la sûreté du moyen mis en oeuvre.

### Chiffrement d'un message pour le rendre confidentiel

- Chiffrer un message avec `gpg`

Vous désirez envoyer un message confidentiel à un de vos collègues `SONNOM_SONPRENOM`. Ecrire dans un fichier texte `messagesecretpoursonnomsonprenom.txt` un message pour `SONNOM_SONPRENOM`. Pour chiffrer un message à destination de `SONNOM_SONPRENOM` il faut utiliser la clé publique de `SONNOM_SONPRENOM`, il vous faut donc récupérer la clé publique de `SONNOM_SONPRENOM` et l'importer manuellement.

- Récupérer et importer manuellement une clé publique

Si votre collègue diffuse sa clé publique sur un site web, un simple `wget http://.../SONNOM_SONPRENOM.pub` fera l'affaire, puis 

    gpg --import SONNOM_SONPRENOM.pub
    gpg --list-keys

- Tester aussi de la supprimer. Attention si vous souhaitez supprimer votre propre clé publique il faudra d'abord supprimer votre clé privée avec --delete-secret-key

    gpg --delete-key IDPUBLICKEY

- Une fois importée la clé publique du destinataire désiré, utilisez la pour chiffrer votre message. Rendre publique le résultat du chiffrement. 

    gpg --output messagesecretpoursonnomsonprenom.txt.gpg --encrypt --recipient IDPUBLICKEY messagesecretpoursonnomsonprenom.txt

- Que signifie l'option `--encrypt` ? Qu'apporte l'adjonction des options `--sign` et `--symmetric` ?

Certaines clés publiques sont disponibles sur des serveurs de clés publiques PGP. Pour récupérer et importer automatiquement une clé publique, il suffit de

    gpg --keyserver wwwkeys.pgp.net --recv-keys IDPUBLICKEY

Ici quelques serveurs de clés publiques PGP :

    https://keyserver.pgp.com/vkd/GetWelcomeScreen.event
    http://pgp.mit.edu


### Déchiffrement d'un message confidentiel avec `gpg`

- Déchiffrer le `messagesecret.txt.gpg` qu'un de vos collègues vous a adressé au vu et au su de tout le monde. Vous devriez être le seul à pouvoir le faire

    gpg --output messagesecretpoursonnomsonprenom.decrypted.txt --decrypt messagesecretpourX.txt.gpg

### Signature numérique d'un fichier avec `gpg`

- Sélectionner le fichier que vous souhaitez signer. Cela peut un fichier texte, un fichier zip d'un répertoire ou ce que vous voulez. On va travailler ici avec un fichier texte `VOTRENOM_VOTREPRENOM.txt` que vous créerez avec le contenu que vous souhaitez.

- Rendre accessible ce fichier à vos collègues. 

Si vous avez mis en place un serveur web, alors placer le fichier dans votre public_html avec les droits adéquates `chmod a+rx VOTRENOM_VOTREPRENOM.txt`.

- Générez une signature détachée de votre fichier et rendez la accessible. Les signatures PGP détachées sont en générale stockées dans des fichiers avec une extension `.sig`, `.gpg` ou `.asc`. Quelle différence y a-t-il entre les options `--detach-sig` et `--sign` et --clearsign` ?

    gpg --output VOTRENOM_VOTREPRENOM.txt.asc --detach-sig VOTRENOM_VOTREPRENOM.txt

- Rendre disponible la signature.

- Récupérer d'un de vos collègues la donnée et la signature.

Si vous tentez de vérifier la signature PGP du fichier sans avoir importé la clé publique de l'auteur du fichier, un message d'erreur vous réclamant la clé publique s'affichera. Ce message indiquera l'identifiant `IDPUBLICKEY` de la clé publique souhaitée. Si vous avez récupéré la clé publique par un quelconque moyen vous pouvez alors l'importer manuellement dans votre trousseau. Sinon vous pouvez tentez de la récupérer et de l'importer automatiquement à partir d'un serveur de clés publiques PGP. 

- Vérifier la signature PGP du fichier.

    gpg --verify SONNOM_SONPRENOM.txt.asc SONNOM_SONPRENOM.txt

---
## Exercice 5 : Vérifier l'intégrité d'un fichier à l'aide de MD5 et SHA (30 minutes)

Générer une empreinte `MD5`, `SHA1` pour votre fichier et les mettre à disposition.

L'environnement Linux dispose des commandes `md5sum` et `sha1sum` et des man(uels) correspondants.

- Combien font de bits les sommes de contrôles MD5 ? Quelle RFC (`Request For Comments`) décrit comment calculer ces sommes ?

    md5sum VOTRENOM_VOTREPRENOM.txt > VOTRENOM_VOTREPRENOM.txt.md5

    sha1sum VOTRENOM_VOTREPRENOM.txt > VOTRENOM_VOTREPRENOM.txt.sha1

- Vérifier l'intégrité MD5 et SHA1 du fichier. Pour vérifier l'intégrité d'un fichier à l'aide de MD5, on génère son empreinte et on la compare avec celle fournie.

    md5sum SONNOM_SONPRENOM.txt

    cat SONNOM_SONPRENOM.txt.md5

Pour information, pour générer l'empreinte MD5 de plusieurs fichiers et vérifier l'intégrité de plusieurs fichiers à la fois, on peut faire comme ci-après :

    # génération
    md5sum * > mesfichiers.md5

    # vérification avec -c, check, -w, warn, et les fichiers à vérifier présents dans le répertoire courant
    md5sum -w -c mesfichiers.md5

Pour vérifier l'intégrité d'un fichier à l'aide de `SHA1`, on agit de manière analogue à `MD5`, on génère son empreinte et on la compare avec celle fournie.

    sha1sum SONNOM_SONPRENOM.txt

    cat SONNOM_SONPRENOM.txt.sha1

Remarque : la commande `cmp` permet de comparer des fichiers octet à octet. 

---
## Exercice 6 : Un plugin PGP pour votre mailer (pour aller plus loin)

Si vous souhaitez utiliser PGP dans votre mailer c'est certainement possible !

Suivre les consignes ici https://emailselfdefense.fsf.org/fr/ (Plugin Enigmail pour Thunderbird : https://enigmail.net).

Pour rappel Thunderbird est disponible dans le menu Applications > Internet de vos systèmes. Le wiki de l'université vous explique comment configurer les serveurs IMAP et SMTP : http://wiki.univ-nantes.fr/doku.php?id=etudiants

A toutes fins utiles, utiliser GPG avec Gmail c'est aussi possible avec les plugins firefox/chrome qui vont bien : Mailvelope, FlowCrypt... Vous pouvez commencer par lire https://webapps.stackexchange.com/questions/58/how-to-use-gpg-with-gmail 
