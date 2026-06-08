package service;

import dto.ProductDto;
import entity.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    public void save(ProductDto dto) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product product = new Product(dto.getName(), dto.getDescription(), dto.getPrice());
            session.persist(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public ProductDto findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Product product = session.find(Product.class, id);
            return product != null ? convertToDto(product) : null;
        }
    }

    public List<ProductDto> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery("FROM Product", Product.class).list();
            return products.stream().map(this::convertToDto).collect(Collectors.toList());
        }
    }

    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product product = session.find(Product.class, id);
            if (product != null) session.remove(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void update(int id, ProductDto dto) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product product = session.find(Product.class, id);
            if (product != null) {
                product.setName(dto.getName());
                product.setDescription(dto.getDescription());
                product.setPrice(dto.getPrice());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    private ProductDto convertToDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product(
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice());
        product.setId(productDto.getId());
        return product;
    }
}
