do
$$
declare
	ind int;
	arow record;
begin
	for arow in (select * from valuepal.holdings) loop

		update valuepal.holdings set avg_purchase_price =
		(select avg(price) from valuepal.orders where ticker = arow.ticker 
		and wallet_id = arow.wallet_id) where ticker=arow.ticker
		and wallet_id = arow.wallet_id;

	end loop;
end;
$$