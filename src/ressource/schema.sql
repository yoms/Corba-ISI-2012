CREATE  TABLE IF NOT EXISTS `Avatar` (

  `idAvatar` INT NOT NULL ,

  `code_acces` VARCHAR(45) NULL ,

  `taille` VARCHAR(45) NULL ,

  `humeur` VARCHAR(45) NULL ,

  `nom` VARCHAR(45) NULL ,

  `pièce_courante` VARCHAR(45) NULL ,

  `est_admin` TINYINT(1) NULL DEFAULT false ,

  `est_connecte` TINYINT(1) NULL DEFAULT false ,

  PRIMARY KEY (`idAvatar`) );
  
CREATE  TABLE IF NOT EXISTS `Post` (

  `idPost` INT NOT NULL ,

  `contenu` VARCHAR(45) NULL ,

  `date_heure` DATETIME NULL ,

  `id_piece` VARCHAR(45) NULL ,

  `id_posteur` INT NULL ,

  PRIMARY KEY (`idPost`) ,

  CONSTRAINT `fk_id_poster`

    FOREIGN KEY (`id_posteur` )

    REFERENCES `Avatar` (`idAvatar` ));
    
    