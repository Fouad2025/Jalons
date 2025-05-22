import java.util.Scanner;
public class assurance {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Le véhicule roulait à quelle vitesse ? ");
        int vitesse = scanner.nextInt();
        System.out.print("Etiez-vous derrière le véhicule en collision ? (oui/non) : ");
        boolean derriere = scanner.next().equalsIgnoreCase("oui");
        System.out.print("Vous aviez combien de kilomètres au compteur ? ");
        int compteur = scanner.nextInt();
        System.out.print("A combien d'années date votre dernier controle technique ? (0 si moins d'un an) : ");
        int dernierControle = scanner.nextInt();
       if (vitesse <130 && !derriere && (compteur > 150000 && dernierControle <= 2 || compteur < 150000 && dernierControle <= 5) ) {
           System.out.println("Vous etes éligible au remplacement de votre véhicule.");
       } else {
           System.out.println("Vous n'êtes pas éligible au remplacement de votre véhicule.");
       }
       scanner.close();
    }
}