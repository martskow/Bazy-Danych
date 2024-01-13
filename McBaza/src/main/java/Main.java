import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        // Wyświetl procent produktów, które dostarczają więcej niż 50% sumy żelaza i wapnia. Zwróć
        // wynik do dwóch miejsc po przecinku

        var queryStringA = "SELECT ROUND(100.00 * COUNT(p) / ((SELECT COUNT(p2) FROM ProductsEntity p2)), 2) FROM ProductsEntity p WHERE p.calcium + p.iron > 50";
        Query queryA = em.createQuery(queryStringA);
        Float resultA = (Float) queryA.getSingleResult();
        System.out.println("Result A: " + resultA);

        // Wyświetl średnią wartość kaloryczną produktów z bekonem w nazwie.

        var queryStringB = "SELECT AVG(p.calories) FROM ProductsEntity p WHERE p.itemName LIKE '%Bacon%'";
        Query queryB = em.createQuery(queryStringB);
        Double resultB = (Double) queryB.getSingleResult();
        System.out.println("Result B: " + resultB);

        // Dla każdej z kategorii wyświetl produkt o największej wartości cholesterolu.

        var queryStringC = "SELECT c.catName, p.itemName, MAX(p.cholesterole) " +
                "FROM CategoriesEntity c JOIN c.productsEntities p " +
                "WHERE p.cholesterole = (SELECT MAX(p1.cholesterole) FROM ProductsEntity p1 WHERE p1.category = c) " +
                "GROUP BY c.catName, p.itemName";

        Query queryC = em.createQuery(queryStringC);
        List<Object[]> resultC = queryC.getResultList();

        System.out.println("Result C: ");
        for (Object[] result : resultC) {
            String categoryName = (String) result[0];
            String itemName = (String) result[1];
            Number maxCholesterol = (Number) result[2];
            System.out.println(categoryName + ", " + itemName + ", " + maxCholesterol);
        }

        // Wyświetl liczbę kaw (Mocha lub Coffee w nazwie) niezawierających błonnika.

        var queryStringD = "SELECT COUNT(*) FROM ProductsEntity p " +
                "WHERE (p.itemName LIKE '%Mocha%' OR p.itemName LIKE '%Coffee%') AND p.fiber = 0";
        Query queryD = em.createQuery(queryStringD);
        Long resultD = (Long) queryD.getSingleResult();
        System.out.println("Result D: " + resultD);

        // Wyświetl kaloryczność wszystkich McMuffinów. Wyniki wyświetl w kilodżulach (jedna kaloria to 4184 dżule) rosnąco.

        var queryStringE = "SELECT p.itemName, p.calories * 4184.00 FROM ProductsEntity p " +
                "WHERE p.itemName LIKE '%McMuffin%' ORDER BY p.calories ASC";
        Query queryE = em.createQuery(queryStringE);
        List<Object[]> resultE = queryE.getResultList();

        System.out.println("Result E: ");
        for (Object[] result : resultE) {
            String itemName = (String) result[0];
            Float kJ = (Float) result[1];
            System.out.println("Item Name: " + itemName + ", kJ: " + kJ);
        }

        // Wyświetl liczbę różnych wartości węglowodanów

        var queryStringF = "SELECT COUNT(DISTINCT p.carbs) FROM ProductsEntity p";;
        Query queryF = em.createQuery(queryStringF);
        Long resultF = (Long) queryF.getSingleResult();
        System.out.println("Result F: " + resultF);

    }
}
