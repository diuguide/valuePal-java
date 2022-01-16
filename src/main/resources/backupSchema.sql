CREATE TRIGGER updateavgpricesingleholding1
    BEFORE
        INSERT
        OR
UPDATE
    ON
    valuepaldev.holdings
    FOR EACH ROW
    EXECUTE PROCEDURE valuepaldev.updateavgprice2();

CREATE OR REPLACE FUNCTION valuepaldev.calcavgprice(
	p_price valuepaldev.holdings.price%TYPE,
	p_wallet_id valuepaldev.holdings.wallet_id%TYPE,
	p_ticker valuepaldev.holdings.ticker%TYPE,
	p_quantity valuepaldev.holdings.quantity%TYPE
)
RETURNS DOUBLE PRECISION
LANGUAGE plpgsql
AS $$
DECLARE
v_total_cost valuepaldev.orders.total_cost%type := 0;
v_quantity valuepaldev.holdings.quantity%type := 0;
v_record record;

BEGIN
SELECT SUM(total_cost) INTO v_total_cost FROM valuepaldev.orders
WHERE ticker = p_ticker
  AND wallet_id = p_wallet_id
  AND status = 'Filled'
  AND type = 'B';

v_total_cost := v_total_cost + p_price;

RETURN v_total_cost / p_quantity;

END;
$$

CREATE OR REPLACE FUNCTION valuepaldev.updateavgprice2()
RETURNS TRIGGER AS $$
DECLARE
rec_count integer;
BEGIN
SELECT
    count(*)
INTO
    rec_count
FROM
    valuepaldev.orders
WHERE
        ticker = NEW.ticker
  AND wallet_id = NEW.wallet_id;

IF(rec_count > 0) THEN
		NEW.avg_purchase_price := valuepaldev.calcavgprice(NEW.last_cost,
		NEW.wallet_id,
		NEW.ticker,
		NEW.quantity);

RETURN NEW;
ELSE
		NEW.avg_purchase_price := NEW.price;
RETURN NEW;
END IF;
END
$$ LANGUAGE plpgsql;
