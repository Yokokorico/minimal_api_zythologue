/* 1. Lister les bières par taux d'alcool, de la plus légère à la plus forte.*/

SELECT * from beer order by zyb_abv;

/* 2.Afficher le nombre de bières par catégorie.*/

SELECT count(zyb_id), zyc_name from beer
	INNER JOIN category on beer.zyb_category = category.zyc_id
	group by zyc_name;

/*3.Trouver toutes les bières d'une brasserie donnée.*/

SELECT zyb_name,zyb_brewery from beer
	INNER JOIN brewery on beer.zyb_brewery = brewery.zybr_id
Where brewery.zybr_name = 'Stehr Inc';

/*4.Lister les utilisateurs et le nombre de bières qu'ils ont ajoutées à leurs favoris.*/

SELECT zyu_id,zyu_name,count(zyf_beer) from favorite
	INNER JOIN "User" on "User".zyu_id = favorite.zyf_user
group by zyu_id order by count(zyf_beer) asc

/*5.Ajouter une nouvelle bière à la base de données.*/

insert into Beer (zyb_name, zyb_abv, zyb_description, zyb_brewery, zyb_category) values 
	('86',
	8.6,
	'8 morts 6 blessés je péte ma bière MA LUBULLE', 
	1,
	1
);

/*6.Afficher les bières et leurs brasseries, ordonnées par pays de la brasserie.*/

SELECT zyb_name,zybr_name,zybr_country from Beer
INNER JOIN Brewery on Brewery.zybr_id = Beer.zyb_brewery
order by zybr_country;

/*7.Lister les bières avec leur nom, et les ingredients.*/

SELECT Beer.zyb_id, Beer.zyb_name, Ingredient.zyi_name 
FROM beerIngredient
INNER JOIN Beer ON Beer.zyb_id = beerIngredient.zybi_beer
INNER JOIN Ingredient ON Ingredient.zyi_id = beerIngredient.zybi_ingredient
order by Beer.zyb_id;

/*8.Afficher les brasseries et le nombre de bières qu'elles produisent, pour celles ayant plus de 5 bières*/

SELECT Brewery.zybr_name, count(Beer.zyb_id) from beer
INNER JOIN Brewery on beer.zyb_brewery = Brewery.zybr_id
group by Brewery.zybr_name
having count(Beer.zyb_id) > 5;

/*9.Lister les bières qui n'ont pas encore été ajoutées aux favoris par aucun utilisateur.*/
SELECT zyu_id,zyu_name from "User"
inner join Favorite on "User".zyu_id = Favorite.zyf_user
where zyu_id not in (select zyf_user from favorite)

/*10.Trouver les bières favorites communes entre deux utilisateurs.*/

SELECT Beer.zyb_id, Beer.zyb_name
FROM Favorite AS Fav1
JOIN Favorite AS Fav2 ON Fav1.zyf_beer = Fav2.zyf_beer
JOIN Beer ON Fav1.zyf_beer = Beer.zyb_id
WHERE Fav1.zyf_user = 46 AND Fav2.zyf_user = 116;

/*11.Afficher les brasseries dont les bières ont une moyenne des notes supérieure à une certaine valeur.*/

SELECT Brewery.zybr_name, avg(zyr_rating) as "Moyenne des review" from Brewery
    inner join Beer on Beer.zyb_brewery = Brewery.zybr_id
	inner join Review on Review.zyr_beer = Beer.zyb_id
group by Brewery.zybr_name
having avg(zyr_rating) > 4;

/*12.Mettre à jour les informations d'une brasserie.*/

update Brewery
	set zybr_name = 'LA BONNE AUBERGE',
	zybr_country = 'LA FRANCE',
	zybr_updated_at = CURRENT_TIMESTAMP
where zybr_id = 1

/*13.Supprimer les photos d'une bière en particulier*/

delete from picture
where zyp_beer = 2
returning zyp_id;


/*14. Ecrire une procédure stockée pour permettre à un utilisateur de noter une bière. Si l'utilisateur a déjà noté cette bière, la note est mise à jour ; sinon, une nouvelle note est ajoutée.*/

CREATE OR REPLACE PROCEDURE insert_or_modify_review(beerlover integer, beer integer, rating integer, review_content varchar(10000))
LANGUAGE plpgsql 
AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM review WHERE zyr_user = beerlover AND zyr_beer = beer) THEN
        UPDATE review 
        SET zyr_review = review_content,
		zyr_updated_at = CURRENT_TIMESTAMP
        WHERE zyr_user = beerlover AND zyr_beer = beer;
    ELSE
        INSERT INTO review (zyr_user, zyr_beer, zyr_rating, zyr_review) VALUES (beerlover, beer, rating, review_content);
    END IF;
END;
$$;

/*15.Ecrire un déclencheur qui permet de détecter si l'ABV (taux d'alcool) est bien compris entre 0 et 20 avant l'ajout de chaque bière*/

CREATE OR REPLACE FUNCTION abv_between_0_20() RETURNS TRIGGER AS $abv_between_0_20$
BEGIN
	IF NEW.zyb_abv < 0 OR NEW.zyb_abv > 20 THEN
		RAISE EXCEPTION 'ABV NOT BETWEEN 0 AND 20';
	END IF;
RETURN NEW;
END;
$abv_between_0_20$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER func_abv_between_0_20
BEFORE INSERT OR UPDATE ON beer
FOR EACH ROW EXECUTE FUNCTION abv_between_0_20();


