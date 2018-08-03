package cn.smbms.service.bill;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;

@Service("billService")
@Lazy
public class BillServiceImpl implements BillService {
	@Resource
	private BillMapper billMapper;

	@Override
	public boolean add(Bill bill) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (billMapper.add(bill) > 0)
			flag = true;
		return flag;
	}

	@Override
	public List<Bill> getBillList(Bill bill) {
		// TODO Auto-generated method stub
		List<Bill> billList = null;

		billList = billMapper.getBillList(bill);
		return billList;
	}

	@Override
	public boolean deleteBillById(String delId) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (billMapper.deleteBillById(delId) > 0)
			flag = true;
		return flag;
	}

	@Override
	public Bill getBillById(String id) {
		// TODO Auto-generated method stub
		Bill bill = null;
		bill = billMapper.getBillById(id);
		return bill;
	}

	@Override
	public boolean modify(Bill bill) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (billMapper.modify(bill) > 0)
			flag = true;
		return flag;
	}

}
