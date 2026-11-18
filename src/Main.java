import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final List<Product> catalog = new ArrayList<>();
    private static ShoppingCart cart = new ShoppingCart();

    public static void main(String[] args) {
        seedCatalog();
        System.out.println("=== Üdv a webshopban! ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Választás", 0, 6);
            System.out.println();

            switch (choice) {
                case 1 -> listCatalog();
                case 2 -> addToCart();
                case 3 -> showCart();
                case 4 -> removeFromCart();
                case 5 -> addNewProduct();
                case 6 -> running = !checkout();
                case 0 -> {
                    System.out.println("Szia, legközelebb is várunk!");
                    running = false;
                }
            }
        }
    }


    private static void listCatalog() {
        System.out.println("--- TERMÉKKATALÓGUS ---");
        System.out.printf("%-4s %-20s %-10s %12s  %s%n", "#", "Név", "Vonalkód", "Ár", "Kategória");
        for (int i = 0; i < catalog.size(); i++) {
            Product p = catalog.get(i);
            System.out.printf("%-4d %-20s %-10s %,9.0f Ft  %s%n",
                    i + 1, p.getName(), p.getBarcode(), p.getPrice(), p.getCategory());
        }
    }

    private static void addToCart() {
        listCatalog();
        System.out.println();
        int idx = readInt("Melyik terméket (sorszám, 0 = mégse)", 0, catalog.size());
        if (idx == 0) {
            return;
        }
        Product p = catalog.get(idx - 1);
        int qty = readInt("Mennyiség (db)", 1, 1000);

        try {
            cart.addItem(new CarItem(p, qty));
            System.out.println("✔ Kosárba téve: " + qty + " db " + p.getName());
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Hiba: " + e.getMessage());
        }
    }

    private static void showCart() {
        List<CarItem> items = cart.getItems();
        if (items.isEmpty()) {
            System.out.println("A kosarad üres.");
            return;
        }
        System.out.println("--- KOSÁR ---");
        double brutto = 0;
        for (int i = 0; i < items.size(); i++) {
            CarItem it = items.get(i);
            System.out.printf("%d. %-20s %3d db x %,8.0f Ft = %,10.0f Ft%n",
                    i + 1, it.getProduct().getName(), it.getQuantity(),
                    it.getProduct().getPrice(), it.subtotal());
            brutto += it.subtotal();
        }
        double total = cart.calculateTotal();
        System.out.println("-".repeat(60));
        System.out.printf("Összesen: %,.0f Ft | Kedvezmény után: %,.0f Ft%n", brutto, total);
        printDiscountHint(brutto);
    }

    private static void removeFromCart() {
        if (cart.getItems().isEmpty()) {
            System.out.println("A kosarad üres, nincs mit törölni.");
            return;
        }
        showCart();
        System.out.println();
        String name = readNonEmpty("Melyik terméket töröljem (név)");
        int before = cart.getItems().size();
        cart.removeItem(name);
        if (cart.getItems().size() < before) {
            System.out.println("✔ Törölve: " + name);
        } else {
            System.out.println("✘ Nincs ilyen nevű tétel a kosárban.");
        }
    }

    private static void addNewProduct() {
        System.out.println("--- ÚJ TERMÉK FELVÉTELE ---");
        String name = readNonEmpty("Név");
        String barcode = readNonEmpty("Vonalkód");
        double price = readDouble("Ár (Ft)", 1);

        ProductCategory[] cats = ProductCategory.values();
        for (int i = 0; i < cats.length; i++) {
            System.out.println("  " + (i + 1) + ") " + cats[i]);
        }
        int c = readInt("Kategória", 1, cats.length);

        Product p = new Product(name, barcode, price, cats[c - 1]);
        catalog.add(p);
        System.out.println("✔ Felvéve: " + p.getName());
    }

    private static boolean checkout() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Üres kosárral hogy szeretnél fizetni?...");
            return false;
        }
        printReceipt(cart);
        String ans = readNonEmpty("Fizetés végrehajtása? (i/n)");
        if (ans.equalsIgnoreCase("i")) {
            System.out.println("\n✔ Köszönjük a vásárlást!");
            return true;
        }
        System.out.println("Fizetés megszakítva, a kosarad megmaradt.");
        return false;
    }


    private static void printReceipt(ShoppingCart c) {
        String line = "=".repeat(54);
        System.out.println(line);
        System.out.printf("%-20s %6s %10s %12s%n", "Termék", "Db", "Egységár", "Részösszeg");
        System.out.println("-".repeat(54));

        double brutto = 0;
        for (CarItem item : c.getItems()) {
            System.out.printf("%-20s %6d %,10.0f %,12.0f%n",
                    item.getProduct().getName(), item.getQuantity(),
                    item.getProduct().getPrice(), item.subtotal());
            brutto += item.subtotal();
        }
        double vegosszeg = c.calculateTotal();

        System.out.println("-".repeat(54));
        System.out.printf("%-40s %,10.0f Ft%n", "Összesen kedvezmény nélkül:", brutto);
        System.out.printf("%-40s %,10.0f Ft%n", "Kedvezmény:", -(brutto - vegosszeg));
        System.out.println(line);
        System.out.printf("%-40s %,10.0f Ft%n", "FIZETENDŐ:", vegosszeg);
        System.out.println(line);
    }

    private static void printDiscountHint(double brutto) {
        if (brutto <= 10000) {
            System.out.printf("Tipp: még %,.0f Ft és 10%% kedvezményt kapsz!%n", 10000 - brutto + 1);
        } else if (brutto <= 25000) {
            System.out.printf("Tipp: még %,.0f Ft és 25%% kedvezményt kapsz!%n", 25000 - brutto + 1);
        } else {
            System.out.println("Megvan a maximális, 25%-os kedvezmény!");
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("--------- MENÜ ---------");
        System.out.println(" 1) Termékek listázása");
        System.out.println(" 2) Termék kosárba tétele");
        System.out.println(" 3) Kosár megtekintése");
        System.out.println(" 4) Termék törlése a kosárból");
        System.out.println(" 5) Új termék felvétele");
        System.out.println(" 6) Fizetés");
        System.out.println(" 0) Kilépés");
        System.out.println("------------------------");
    }


    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " [" + min + "-" + max + "]: ");
            String s = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n >= min && n <= max) {
                    return n;
                }
                System.out.println("  ✘ A szám " + min + " és " + max + " között legyen!");
            } catch (NumberFormatException e) {
                System.out.println("  ✘ Ez nem szám, próbáld újra!");
            }
        }
    }

    private static double readDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = sc.nextLine().trim().replace(',', '.');
            try {
                double d = Double.parseDouble(s);
                if (d >= min) {
                    return d;
                }
                System.out.println("  ✘ Legalább " + min + " legyen!");
            } catch (NumberFormatException e) {
                System.out.println("  ✘ Ez nem szám, próbáld újra!");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("  ✘ Nem lehet üres!");
        }
    }


    private static void seedCatalog() {
        catalog.add(new Product("Tej",         "5990001", 450,    ProductCategory.HAZTARTAS));
        catalog.add(new Product("Kenyér",      "5990002", 690,    ProductCategory.ELELMISZER));
        catalog.add(new Product("Póló",        "5990004", 4990,   ProductCategory.RUHAZAT));
        catalog.add(new Product("Fejhallgató", "5990005", 12990,  ProductCategory.ELEKTRONIKA));
        catalog.add(new Product("Okos TV",     "5990003", 189990, ProductCategory.ELEKTRONIKA));
    }
}