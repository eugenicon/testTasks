import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shopfactory.common.Util;
import com.shopfactory.data.JdbcUtil;
import com.shopfactory.data.ShopDAO;
import com.shopfactory.data.ShopService;
import com.shopfactory.domain.Category;
import com.shopfactory.domain.Product;
import com.shopfactory.domain.Shop;

public class App {
	
	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {
		
		Properties properties = Util.readPropertiesFromResource("/jdbc.properties");
		
		try(Connection conn = JdbcUtil.getConnection(
				properties.getProperty("driver"), 
				properties.getProperty("url"), 
				properties.getProperty("username"), 
				properties.getProperty("password"));) {
			
			ShopDAO shopDAO = new ShopDAO(conn);
			ShopService shopService = new ShopService(shopDAO);
			
			List<Shop> shops = shopService.getShops();
			
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(shops.size());			
			int delay = 0;
			for (Shop shop : shops) {
				List<Category> categories = shopService.getCategories(shop);
				
				LOGGER.log(Level.INFO, "Creating products for " + shop);
				List<Product> products = shopService.createProducts(categories);
				
				Thread shopThread = new Thread(() -> {
					LOGGER.log(Level.INFO, "Starting " + shop);
					
					LOGGER.log(Level.INFO, "Setting absent products for " + shop);
					shopService.setProductsAbsent(products);
					
					LOGGER.log(Level.INFO, "Setting expected products for " + shop);
					shopService.setProductsExpected(products);
					
					LOGGER.log(Level.INFO, "Modifying prices for " + shop);
					shopService.modifyPrice(products, 1.2);
					
					LOGGER.log(Level.INFO, "Finished " + shop);
				});

				executor.schedule(shopThread, delay, TimeUnit.SECONDS);
				delay = 10;
			}
			
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		
		LOGGER.log(Level.INFO, "Finished");
	}

}
