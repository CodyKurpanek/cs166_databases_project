CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION insert_manager_or_user()
 RETURNS trigger AS
 $BODY$
 BEGIN
 
 IF (NEW.type = 'Manager') THEN
  INSERT INTO ManagerUser (Managerlogin) VALUES (NEW.login);
 ELSIF (NEW.type = 'Employee') THEN
  INSERT INTO EmployeeUser (Employeelogin) VALUES (NEW.login); 
 END IF;
 return NEW;
 

 END;
 $BODY$
 LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION change_manager_or_user_login()
 RETURNS trigger AS 
 $BODY$
 BEGIN

 IF (OLD.type = 'Manager') THEN
  UPDATE ManagerUser SET login = NEW.login WHERE login = OLD.login;
 ELSIF (OLD.type = 'Employee') THEN
  UPDATE EmployeeUser SET login = NEW.login WHERE login = OLD.login;
 END IF;
 return NEW;


 END;
 $BODY$
 LANGUAGE plpgsql VOLATILE;
 



DROP TRIGGER IF EXISTS insert_into_users ON Users;
CREATE TRIGGER insert_into_users AFTER INSERT
ON Users
FOR EACH ROW
EXECUTE PROCEDURE insert_manager_or_user();


DROP TRIGGER IF EXISTS update_login ON Users;
CREATE TRIGGER change_user_login BEFORE UPDATE
OF login ON Users
FOR EACH ROW
EXECUTE PROCEDURE change_manager_or_user_login();

