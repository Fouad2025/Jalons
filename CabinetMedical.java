import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CabinetMedical {
   public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<List<String>> rendezVousList = new ArrayList<>();
    System.out.println("Bienvenue dans le système de gestion des rendez-vous du cabinet médical!");

    while (true) {
        System.out.println("\n1. Ajouter un rendez-vous");
        System.out.println("2. Annuler un rendez-vous");
        System.out.println("3. Décaler un rendez-vous");
        System.out.println("4. Afficher la liste des rendez-vous");
        System.out.println("5. Rechercher un rendez-vous par code de référence");
        System.out.println("6. Quitter");
        System.out.print("Votre choix : ");
        int choix;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide.");
            continue;
        }
        switch (choix) {
            case 1:
                ajouterRendezVous(sc, rendezVousList);
                break;
            case 2:
                annulerRendezVous(sc, rendezVousList);
                break;
            case 3:
                decalerRendezVous(sc, rendezVousList);
                break;
            case 4:
                afficherRendezVous(rendezVousList);
                break;
            case 5:
                rechercherRendezVous(sc, rendezVousList);
                break;
            case 6:
                System.out.println("Merci d'avoir utilisé le système de gestion des rendez-vous. À bientôt !");
                sc.close();
                return;
            default:
                System.out.println("Choix invalide.");
        }
    }
}

    private static void ajouterRendezVous(Scanner sc, List<List<String>> rendezVousList) {
        System.out.print("Nom du patient: ");
        String nomPatient = sc.nextLine();
        while (!nomPatient.matches("[a-zA-ZÀ-ÿ\\-\\s]+") || nomPatient.isEmpty()) {
            System.out.println("Le nom du patient ne peut pas contenir de caractères spéciaux !");
            System.out.print("Veuillez entrer un nom valide: ");
            nomPatient = sc.nextLine();
        }

        System.out.print("Prénom du patient: ");
        String prenomPatient = sc.nextLine();
       while (!prenomPatient.matches("[\\p{L}\\-\\s]+") || prenomPatient.isEmpty()) {
            System.out.println("Le prénom du patient ne peut pas contenir de caractères spéciaux !");
            System.out.print("Veuillez entrer un prénom valide: ");
            prenomPatient = sc.nextLine();
        }

        int age;
        while (true) {
            System.out.print("Entrez l'âge du patient: ");
            String ageStr = sc.nextLine();
            try {
                age = Integer.parseInt(ageStr);
                if (age < 0 || age > 120) {
                    System.out.println("Âge invalide ! Veuillez entrer un âge entre 0 et 120.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide pour l'âge.");
            }
        }

        System.out.println("Type de consultation :");
        System.out.println("1. Bilan de santé");
        System.out.println("2. Cardiologie");
        System.out.println("3. Vaccinations");
        System.out.println("4. Certification médicale");
        System.out.println("5. Médecine générale");
        System.out.println("6. Suivi médical");

        String typeConsultation = "";
        String codeType = "";
        double prix = 0.0;
        while (true) {
            System.out.print("Votre choix (1-6) : ");
            String choixType = sc.nextLine();
            switch (choixType) {
                case "1":
                    typeConsultation = "Bilan de santé";
                    codeType = "BS";
                    prix = 120.0;
                    break;
                case "2":
                    typeConsultation = "Cardiologie";
                    codeType = "CA";
                    prix = 200.0;
                    break;
                case "3":
                    typeConsultation = "Vaccinations";
                    codeType = "VC";
                    prix = 0.0;
                    break;
                case "4":
                    typeConsultation = "Certification médicale";
                    codeType = "CM";
                    prix = 100.0;
                    break;
                case "5":
                    typeConsultation = "Médecine générale";
                    codeType = "MG";
                    prix = 70.0;
                    break;
                case "6":
                    typeConsultation = "Suivi médical";
                    codeType = "SM";
                    prix = 60.0;
                    break;
                default:
                    System.out.println("Veuillez entrer un choix valide (1-6) !");
                    continue;
            }
            break;
        }

        String initiales = nomPatient.substring(0, 1).toUpperCase() + prenomPatient.substring(0, 1).toUpperCase();

        LocalDate date;
        while (true) {
            System.out.print("Entrez la date (dd/MM/yyyy): ");
            String dateStr = sc.nextLine();
            try {
                if (dateStr.isEmpty()) {
                    System.out.println("Date ne peut pas être vide !");
                    continue;
                }
                date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("La date du rendez-vous ne peut pas être dans le passé !");
                    continue;
                }

                if (date.getYear() >= 2028) {
                    System.out.println("Date trop éloignée !");
                    continue;
                }
                if (date.getDayOfWeek().getValue() >= 6) {
                    System.out.println("Impossible de prendre rendez-vous le week-end !");
                    continue;
                }
                if (isJourFerie(date)) {
                    System.out.println("Impossible de prendre rendez-vous un jour férié !");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Format de date invalide !");
            }
        }
        String datestr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String code = initiales + datestr + codeType;

        String timeStr;
        int heure, minute;
        while (true) {
            System.out.print("Entrez l'heure (HH:mm): ");
            timeStr = sc.nextLine();
            if (timeStr.isEmpty()) {
                System.out.println("L'heure ne peut pas être vide !");
                continue;
            }
            if (!timeStr.matches("\\d{1,2}:\\d{2}")) {
                System.out.println("Format d'heure invalide ! Utilisez HH:mm.");
                continue;
            }
            try {
                String[] parts = timeStr.split(":");
                heure = Integer.parseInt(parts[0]);
                minute = Integer.parseInt(parts[1]);
                boolean matin = (heure >= 8 && heure < 12);
                boolean apresMidi = (heure >= 14 && heure < 17);
                if (!(matin || apresMidi) || minute < 0 || minute > 59) {
                    System.out.println("L'heure doit être entre 08:00-12:00 ou 14:00-17:00 !");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Format d'heure invalide !");
            }
        }

        LocalTime heureRdv = LocalTime.of(heure, minute);

// Vérifie les conflits de créneaux (30 min d'écart minimum)
boolean conflit;
do {
    conflit = false;
    for (List<String> existing : rendezVousList) {
        if (existing.get(2).equals(date.toString())) {
            LocalTime heureExistante = LocalTime.parse(existing.get(3));
            if (Math.abs(heureRdv.until(heureExistante, java.time.temporal.ChronoUnit.MINUTES)) < 30) {
                conflit = true;
                break;
            }
        }
    }
    if (conflit) {
         System.out.println("Un rendez-vous existe déjà à cette date et heure !");
        System.out.println("Il doit y avoir au moins 30 minutes entre chaque rendez-vous le même jour !");
        System.out.print("Veuillez entrer une nouvelle heure (HH:mm) : ");
        timeStr = sc.nextLine();
        if (timeStr.isEmpty()) {
            System.out.println("L'heure ne peut pas être vide !");
            return;
        }
        try {
            String[] parts = timeStr.split(":");
            heure = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
            boolean matin = (heure >= 8 && heure < 12);
            boolean apresMidi = (heure >= 14 && heure < 17);
            if (!(matin || apresMidi) || minute < 0 || minute > 59) {
                System.out.println("L'heure doit être entre 08:00-12:00 ou 14:00-17:00 !");
                continue;
            }
            heureRdv = LocalTime.of(heure, minute);
        } catch (NumberFormatException e) {
            System.out.println("Format d'heure invalide !");
            return;
        }
    }
} while (conflit);

        // Vérifie les doublons
        boolean doublon = false;
        for (List<String> existing : rendezVousList) {
            if (existing.get(2).equals(date.toString()) && existing.get(3).equals(heureRdv.toString())) {
                doublon = true;
                break;
            }
        }
        while (doublon) {
            System.out.println("Un rendez-vous existe déjà à cette date et heure !");
            System.out.print("Veuillez entrer une nouvelle heure (HH:mm) (au moins 30 min après l'heure précédente) : ");
            timeStr = sc.nextLine();
            if (timeStr.isEmpty()) {
                System.out.println("L'heure ne peut pas être vide !");
                return;
            }
            try {
                String[] parts = timeStr.split(":");
                int newHeure = Integer.parseInt(parts[0]);
                int newMinute = Integer.parseInt(parts[1]);
                LocalTime nouvelleHeure = LocalTime.of(newHeure, newMinute);
                if (!nouvelleHeure.isAfter(heureRdv.plusMinutes(29))) {
                    System.out.println("La nouvelle heure doit être au moins 30 minutes après l'horaire précédemment demandé (" + heureRdv + ").");
                    continue;
                }
                heureRdv = nouvelleHeure;
            } catch (NumberFormatException e) {
                System.out.println("Format d'heure invalide !");
                return;
            }
            doublon = false;
            for (List<String> existing : rendezVousList) {
                if (existing.get(2).equals(date.toString()) && existing.get(3).equals(heureRdv.toString())) {
                    doublon = true;
                    break;
                }
            }
        }

        // Stockage du rendez-vous : [code, nom, date, heure, prénom, age, type, prix]
        List<String> rdv = new ArrayList<>();
        rdv.add(code);
        rdv.add(nomPatient);
        rdv.add(date.toString());
        rdv.add(heureRdv.toString());
        rdv.add(prenomPatient);
        rdv.add(String.valueOf(age));
        rdv.add(typeConsultation);
        rdv.add(String.valueOf(prix));
        rendezVousList.add(rdv);

        System.out.println("Rendez-vous ajouté avec succès ! Code de référence : " + code);
        System.out.printf("Prix initial de la consultation : %.2f €%n", prix);

        if (age < 18 || age > 60) {
            double prixReduit = prix * 0.2;
            System.out.printf("Prix après prise en charge Assurance Maladie (80%%) : %.2f €%n", prixReduit);
        } else {
            double prixReduit = prix * 0.4;
           System.out.printf("Prix après prise en charge Assurance Maladie (60%%) : %.2f \u20AC%n", prixReduit);
        }
        System.out.println("Rendez-vous créé : " + nomPatient + " " + prenomPatient + ", " + age + " ans, le " +
                date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " à " + heureRdv.format(DateTimeFormatter.ofPattern("HH:mm")) +
                ", code : " + code);
    }

    private static void annulerRendezVous(Scanner sc, List<List<String>> rendezVousList) {
        System.out.print("Entrez le code de référence du rendez-vous à annuler: ");
        String code = sc.nextLine();
        List<String> rdv = trouverRendezVous(code, rendezVousList);
        if (rdv != null) {
            rendezVousList.remove(rdv);
            System.out.println("Rendez-vous annulé.");
        } else {
            System.out.println("Rendez-vous non trouvé.");
        }
    }

private static void decalerRendezVous(Scanner sc, List<List<String>> rendezVousList) {
    System.out.print("Entrez le code de référence du rendez-vous à décaler: ");
    String code = sc.nextLine();
    List<String> rdv = trouverRendezVous(code, rendezVousList);
    if (rdv == null) {
        System.out.println("Rendez-vous non trouvé.");
        return;
    }
    String oldDate = rdv.get(2);
    String oldHeure = rdv.get(3);

    LocalDate newDate;
    while (true) {
        System.out.print("Entrez la nouvelle date (dd/MM/yyyy): ");
        String newDateStr = sc.nextLine();
        if (newDateStr.isEmpty()) {
            System.out.println("La nouvelle date ne peut pas être vide !");
            continue;
        }
        try {
            newDate = LocalDate.parse(newDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            System.out.println("Format de date invalide !");
            continue;
        }
        if (newDate.isBefore(LocalDate.now())) {
            System.out.println("La date du rendez-vous ne peut pas être dans le passé !");
            continue;
        }
        if (newDate.getDayOfWeek().getValue() >= 6) {
            System.out.println("Impossible de prendre rendez-vous le week-end !");
            continue;
        }
        if (isJourFerie(newDate)) {
            System.out.println("Impossible de prendre rendez-vous un jour férié !");
            continue;
        }
        break;
    }

    // Boucle pour obtenir une nouvelle heure valide et libre
    while (true) {
    System.out.print("Entrez la nouvelle heure (HH:mm): ");
    String newTimeStr = sc.nextLine();
    if (newTimeStr.isEmpty()) {
        System.out.println("La nouvelle heure ne peut pas être vide !");
        continue;
    }
    if (!newTimeStr.matches("\\d{1,2}:\\d{2}")) {
        System.out.println("Format d'heure invalide ! Utilisez HH:mm.");
        continue;
    }
    LocalTime newTime;
    try {
        String[] parts = newTimeStr.split(":");
        int heure = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        newTime = LocalTime.of(heure, minute);
    } catch (NumberFormatException e) {
        System.out.println("Format d'heure invalide !");
        continue;
    }

    int essais = 0;
    boolean conflit;
    do {
        // Vérifie la plage horaire
        boolean matin = (newTime.getHour() >= 8 && newTime.getHour() < 12);
        boolean apresMidi = (newTime.getHour() >= 14 && newTime.getHour() < 17);
        if (!(matin || apresMidi) || newTime.getMinute() < 0 || newTime.getMinute() > 59) {
            System.out.println("L'heure doit être entre 08:00-12:00 ou 14:00-17:00 !");
            break;
        }
        // Refuse 17h00 ou plus
        if (newTime.isAfter(LocalTime.of(16, 30))) {
            System.out.println("Impossible de décaler après 16:30, le bureau ferme à 17:00.");
            break;
        }
        // Vérifie si la nouvelle date et heure sont identiques à l'ancien rendez-vous
        if (oldDate.equals(newDate.toString()) && oldHeure.equals(newTime.toString())) {
            System.out.println("La nouvelle date et la nouvelle heure sont identiques à l'ancien rendez-vous. Veuillez choisir un autre créneau.");
            break;
        }
        // Vérifie les conflits
        conflit = false;
        for (List<String> existing : rendezVousList) {
            if (existing == rdv) continue;
            if (existing.get(2).equals(newDate.toString()) && existing.get(3).equals(newTime.toString())) {
                conflit = true;
                break;
            }
        }
        if (!conflit) {
            // Créneau trouvé
            rdv.set(2, newDate.toString());
            rdv.set(3, newTime.toString());
            System.out.println("Rendez-vous décalé avec succès au " + newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " à " + newTime.format(DateTimeFormatter.ofPattern("HH:mm")) + ".");
            return;
        } else {
            if (newTime.equals(LocalTime.of(17, 00))) {
                System.out.println("Impossible de décaler après 17:00, le cabinet médical ferme à 17:00.");
                break;
            }
            System.out.println("Un rendez-vous existe déjà à cette date et heure, décalage automatique de 30 minutes...");
            newTime = newTime.plusMinutes(30);
            essais++;
            if (essais > 20) {
                System.out.println("Aucun créneau disponible après plusieurs tentatives.");
                return;
            }
        }
    } while (conflit);
}
}
    private static void afficherRendezVous(List<List<String>> rendezVousList) {
        if (rendezVousList.isEmpty()) {
            System.out.println("Aucun rendez-vous à afficher.");
        } else {
            System.out.println("Liste des rendez-vous :");
            for (List<String> rdv : rendezVousList) {
                System.out.println("Nom : " + rdv.get(1) + ", Prénom : " + rdv.get(4) + ", Age : " + rdv.get(5) +
                        " ans, Code : " + rdv.get(0) + ", Date : " +
                        LocalDate.parse(rdv.get(2)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                        ", Heure : " + LocalTime.parse(rdv.get(3)).format(DateTimeFormatter.ofPattern("HH:mm")));
            }
        }
    }

   private static void rechercherRendezVous(Scanner sc, List<List<String>> rendezVousList) {
    System.out.print("Entrez le code de référence : ");
    String saisie = sc.nextLine().trim();

    if (saisie.length() == 2 && saisie.matches("[a-zA-Z]{2}")) {
        String initialesRecherche = saisie.toUpperCase();
        boolean trouve = false;
        for (List<String> rdv : rendezVousList) {
            String nom = rdv.get(1);
            String prenom = rdv.get(4);
            if (!nom.isEmpty() && !prenom.isEmpty()) {
                String initiales = nom.substring(0, 1).toUpperCase() + prenom.substring(0, 1).toUpperCase();
                if (initiales.equals(initialesRecherche)) {
                    System.out.println("Nom : " + nom + ", Prénom : " + prenom + ", Age : " + rdv.get(5) +
                            " ans, Code : " + rdv.get(0) + ", Date : " +
                            LocalDate.parse(rdv.get(2)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                            ", Heure : " + LocalTime.parse(rdv.get(3)).format(DateTimeFormatter.ofPattern("HH:mm")));
                    trouve = true;
                }
            }
        }
        if (!trouve) {
            System.out.println("Aucun rendez-vous trouvé pour ces initiales.");
        }
    } else {
        List<String> rdv = trouverRendezVous(saisie, rendezVousList);
        if (rdv != null) {
            System.out.println("Rendez-vous trouvé : Nom : " + rdv.get(1) + ", Prénom : " + rdv.get(4) + ", Age : " + rdv.get(5) +
                    " ans, Code : " + rdv.get(0) + ", Date : " +
                    LocalDate.parse(rdv.get(2)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    ", Heure : " + LocalTime.parse(rdv.get(3)).format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            System.out.println("Rendez-vous non trouvé.");
        }
    }
}

    private static List<String> trouverRendezVous(String code, List<List<String>> rendezVousList) {
        for (List<String> rdv : rendezVousList) {
            if (rdv.get(0).toLowerCase().contains(code.toLowerCase())) {
                return rdv;
            }
        }
        return null;
    }

    private static boolean isJourFerie(LocalDate date) {
        int year = date.getYear();
        List<LocalDate> joursFeries = new ArrayList<>();
        joursFeries.add(LocalDate.of(year, 1, 1));
        joursFeries.add(LocalDate.of(year, 5, 1));
        joursFeries.add(LocalDate.of(year, 5, 8));
        joursFeries.add(LocalDate.of(year, 7, 14));
        joursFeries.add(LocalDate.of(year, 8, 15));
        joursFeries.add(LocalDate.of(year, 11, 1));
        joursFeries.add(LocalDate.of(year, 11, 11));
        joursFeries.add(LocalDate.of(year, 12, 25));
        // Pâques et jours associés
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int moisPaques = (h + l - 7 * m + 114) / 31;
        int jourPaques = ((h + l - 7 * m + 114) % 31) + 1;
        LocalDate paques = LocalDate.of(year, moisPaques, jourPaques);
        joursFeries.add(paques);
        joursFeries.add(paques.plusDays(1));
        joursFeries.add(paques.plusDays(39));
        joursFeries.add(paques.plusDays(50));
        return joursFeries.contains(date);
    }
}