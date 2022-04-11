select * from valuepal.getavgpurchaseprice('GME', 1);

select * from valuepal.orders o 
where ticker = 'GME'
and wallet_id = 1;

CREATE OR REPLACE FUNCTION valuepal.getavgpurchaseprice(p_ticker character varying, p_wallet_id integer)
 RETURNS double precision
 LANGUAGE plpgsql
AS $$
declare
	v_running_total double precision:=0;
	v_running_quantity integer:=0;
	v_rec record;
	v_running_avg double precision:=0;
	v_buy_total integer :=0;
begin
		raise notice 'ticker: %', p_ticker;
		for v_rec in (select * from valuepal.orders
						where 
						ticker = p_ticker AND
						wallet_id = p_wallet_id AND
						status = 'Filled' AND
						type = 'B') loop
			raise notice '---------------------------------';
			raise notice 'ticker: %', v_rec.ticker;	
			v_running_quantity := v_running_quantity + v_rec.quantity;
			raise notice 'quantity: %', v_running_quantity;
			v_running_total := v_running_total + (v_rec.price * v_rec.quantity);
			raise notice 'total: %', v_running_total;
			end loop;
		
			v_running_avg:=v_running_total/v_running_quantity;
		raise notice '%',v_running_avg;
			for v_rec in (select * from valuepal.orders where type = 'S'
												and ticker = p_ticker
												and status = 'Filled'
												and wallet_id = p_wallet_id) loop
													raise notice '------------------------------------------';
													raise notice 'ticker: %', v_rec.ticker;
													v_running_total:=v_running_total - (v_rec.price * v_rec.quantity);
													raise notice 'v_running_total.2: %', v_running_total;
													v_running_quantity:=v_running_quantity - v_rec.quantity;
													raise notice 'v_running_quantity.2: %', v_running_quantity;
												
											end loop
											;
			
			return v_running_total / v_running_quantity;
		
end;
$$
;

select * from valuepal.getavgpurchaseprice('GME',1);
------------------------------------------------
-------------------------------------------------
-------------------------------------------------------

CREATE OR REPLACE FUNCTION valuepal.getavgpurchaseprice(p_ticker character varying, p_wallet_id integer)
 RETURNS double precision
 LANGUAGE plpgsql
AS $$
declare
	v_running_total double precision:=0;
	v_running_quantity integer:=0;
	v_rec record;
begin
		raise notice 'ticker: %', p_ticker;
		for v_rec in (select * from valuepal.orders
						where 
						ticker = p_ticker AND
						wallet_id = p_wallet_id AND
						status = 'Filled' AND
						type = 'B') loop
			raise notice '---------------------------------';
			raise notice 'ticker: %', v_rec.ticker;	
			v_running_quantity := v_running_quantity + v_rec.quantity;
			raise notice 'quantity: %', v_running_quantity;
			v_running_total := v_running_total + (v_rec.price * v_rec.quantity);
			raise notice 'total: %', v_running_total;
			end loop;
			
			return v_running_total / v_running_quantity;
		
end;
$$
;
