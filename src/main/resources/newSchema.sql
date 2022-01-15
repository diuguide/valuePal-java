CREATE TRIGGER updateavgpricesingleholding1
    BEFORE
        INSERT
        OR
UPDATE
    ON
    valuepaldev.holdings
    FOR EACH ROW
    EXECUTE PROCEDURE valuepaldev.updateavgprice2();

DROP TRIGGER updateavgpricesingleholding1 on valuepaldev.holdings;

CREATE OR REPLACE FUNCTION valuepaldev.calcavgprice(
	p_total_value valuepaldev.holdings.total_value%TYPE,
	p_wallet_id valuepaldev.holdings.wallet_id%TYPE,
	p_ticker valuepaldev.holdings.ticker%TYPE,
	p_quantity valuepaldev.holdings.quantity%TYPE
)
RETURNS DOUBLE PRECISION
LANGUAGE plpgsql
AS $$
DECLARE
v_avg valuepaldev.holdings.avg_purchase_price%TYPE;
v_quantity valuepaldev.holdings.quantity%TYPE;

BEGIN
SELECT
    sum(price), sum(quantity)
INTO
    v_avg, v_quantity
FROM
    valuepaldev.orders
WHERE
        ticker = p_ticker
  AND wallet_id = p_wallet_id
  AND status = 'Filled'
  AND type = 'B';

v_avg := v_avg + p_total_value;
  v_quantity := v_quantity + p_quantity;

RETURN v_avg / v_quantity;
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
		NEW.avg_purchase_price := valuepaldev.calcavgprice(NEW.total_value,
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
