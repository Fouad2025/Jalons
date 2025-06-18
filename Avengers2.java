import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Avengers2 {
    private static final String IDENTIFIANT_CORRECT = "eudeskonda";
    private static final String MOT_DE_PASSE_CORRECT = "konda2025";

    public static void main(String[] args) {
        // Initialisation
        System.out.println("Compagnie AIRMESS : Bonjour!");
        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatHeure = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);

        // Saisie de l'identifiant
        System.out.println("Bienvenue dans notre système de réservation de vols.");

        if (!authentifierUtilisateur(scanner)) {
            System.out.println("Accès refusé.");
            scanner.close();
            return;
        }

        boolean reserverEncore = true;
        while (reserverEncore) {
            int nbPlaces = 0;

            // Affichage de l'en-tête
            System.out.println("\n=== Système de réservation de vols ===");
            System.out.println("Date et heure actuelles: " + maintenant.format(formatHeure) + "\n");

            // Saisie des informations
            System.out.print("Ville de départ : ");
            String villeDepart = scanner.nextLine();

            System.out.print("Pays de départ : ");
            String paysDepart = scanner.nextLine();

            System.out.print("Ville d'arrivée : ");
            String villeArrivee = scanner.nextLine();

            System.out.print("Pays d'arrivée : ");
            String paysArrivee = scanner.nextLine();

            // Saisie et validation de la date
            LocalDate dateDepart = null;
            while (dateDepart == null) {
                System.out.print("Date de départ (JJ/MM/AAAA) : ");
                try {
                    dateDepart = LocalDate.parse(scanner.nextLine(), formatDate);
                    if (dateDepart.isBefore(LocalDate.now())) {
                        System.out.println("Erreur : La date de départ doit être dans le futur.");
                        dateDepart = null;
                    }
                } catch (Exception e) {
                    System.out.println("Format de date invalide. Veuillez réessayer.");
                }
            }

            // Saisie de l'heure
            System.out.print("Heure de départ (HH:MM) : ");
            String heureDepart = scanner.nextLine();

            // Saisie des autres informations
            System.out.print("Durée du vol (heures) : ");
            double dureeVol = scanner.nextDouble();

            System.out.print("Prix du billet (par place) : ");
            double prixBase = scanner.nextDouble();

            while (nbPlaces < 80 || nbPlaces > 200) {
                System.out.print("Nombre de places (80 à 200) : ");
                nbPlaces = scanner.nextInt();
            }
            scanner.nextLine(); // Nettoyage du buffer

            // Calculs
            LocalDate aujourdHui = LocalDate.now();
            long joursAvantDepart = ChronoUnit.DAYS.between(aujourdHui, dateDepart);
            long moisAvantDepart = ChronoUnit.MONTHS.between(aujourdHui, dateDepart);

            // Application des remises/suppléments
            double coefficient = 1.0;

            if (joursAvantDepart < 7) {
                coefficient *= 1.4;  // +40% dernière minute
            } else if (moisAvantDepart > 6) {
                coefficient *= 0.60;  // -40% réservation très anticipée
            }

            if (nbPlaces >= 150) {
                coefficient *= 0.90;  // -10% groupe important
            } else if (nbPlaces < 100) {
                coefficient *= 1.10;   // +10% petit groupe
            }

            double prixFinal = prixBase * coefficient;
            double prixTotal = prixFinal * nbPlaces;

            // Affichage du récapitulatif
            System.out.println("\n=== Récapitulatif de réservation ===");
            System.out.println("Trajet : " + villeDepart + " (" + paysDepart + ") → "
                             + villeArrivee + " (" + paysArrivee + ")");
            System.out.println("Date et heure : " + dateDepart.format(formatDate) + " à " + heureDepart);
            System.out.println("Durée du vol : " + dureeVol + " heures");
            System.out.println("Nombre de places : " + nbPlaces);
            System.out.println("\nDétail du prix :");
            System.out.printf("- Prix de base : %.2f euros\n", prixBase);
            System.out.printf("- Prix après ajustement : %.2f euros (x%d places)\n", prixFinal, nbPlaces);
            System.out.printf("TOTAL : %.2f euros\n", prixTotal);

            // Demander si l'utilisateur veut réserver un autre vol
            System.out.print("\nVoulez-vous réserver un autre vol ? (oui/non) : ");
            String reponse = scanner.nextLine().trim().toLowerCase();
            reserverEncore = reponse.equals("oui") || reponse.equals("o");
        }

        System.out.println("\nMerci d'avoir utilisé notre système de réservation !");
        scanner.close();
    }

    private static boolean authentifierUtilisateur(Scanner scanner) {
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;

        while (tentatives < MAX_TENTATIVES) {
            System.out.print("Entrez votre identifiant : ");
            String identifiant = scanner.nextLine();

            System.out.print("Entrez votre mot de passe : ");
            String mdp = scanner.nextLine();

            if (IDENTIFIANT_CORRECT.equals(identifiant) && MOT_DE_PASSE_CORRECT.equals(mdp)) {
                System.out.println("Authentification réussie !");
                return true;
            }

            tentatives++;
            System.out.println("Identifiants incorrects. Tentatives restantes: " + (MAX_TENTATIVES - tentatives));
        }

        return false;
    }
}