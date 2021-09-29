package com.JimsonBobson.SocialNetwork;

import com.JimsonBobson.SocialNetwork.model.entity.StatusUpdate;
import com.JimsonBobson.SocialNetwork.model.repository.StatusUpdateDao;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Calendar;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class StatusTest {

	@Autowired
	private StatusUpdateDao statusUpdateDao;

	@Test
	void contextSave() {
		StatusUpdate status = new StatusUpdate("This is a test");

		statusUpdateDao.save(status);

		Assert.assertNotNull("non-null id", status.getId());
		Assert.assertNotNull("non-null date", status.getAdded());

		StatusUpdate retrieved = statusUpdateDao.findById(status.getId()).orElse(null);

		Assert.assertEquals("Matching StatusUpdate", status, retrieved);

	}

	@Test
	public void testFindLatest() {
		Calendar calendar = Calendar.getInstance();

		StatusUpdate lastStatusUpdate = null;

		for (int i=0; i<10; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);

			StatusUpdate status = new StatusUpdate("Status update " + i, calendar.getTime());

			statusUpdateDao.save(status);

			lastStatusUpdate = status;
		}

		StatusUpdate retrieved = statusUpdateDao.findFirstByOrderByAddedDesc();

		Assert.assertEquals("Latest status update", lastStatusUpdate, retrieved);
	}

}
