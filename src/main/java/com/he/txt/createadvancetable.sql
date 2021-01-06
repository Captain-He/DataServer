DROP TABLE IF EXISTS XG_TS_BT;
CREATE TABLE SENSORDATA(
	sensorDataID bigint(20) NOT NULL AUTO_INCREMENT,
	sourceAddr int(11) DEFAULT NULL,
	groupAddr int(11) DEFAULT NULL,
	samplingTime bigint(20) DEFAULT NULL,
	item1 double DEFAULT NULL,
	item2 double DEFAULT NULL,
	item3 double DEFAULT NULL,
	item4 double DEFAULT NULL,
item5 double DEFAULT NULL,
	item6 double DEFAULT NULL,
	item7 double DEFAULT NULL,
	item8 double DEFAULT NULL,
item9 double DEFAULT NULL,
	item10 double DEFAULT NULL,
	item11 double DEFAULT NULL,
	item12 double DEFAULT NULL,
item13 double DEFAULT NULL,
	item14 double DEFAULT NULL,
	item15 double DEFAULT NULL,
	item16 double DEFAULT NULL,
item17 double DEFAULT NULL,
	item18 double DEFAULT NULL,
	item19 double DEFAULT NULL,
	item20 double DEFAULT NULL,
item21 double DEFAULT NULL,
	item22 double DEFAULT NULL,
	item23 double DEFAULT NULL,
	item24 double DEFAULT NULL,
item25 double DEFAULT NULL,
	item26 double DEFAULT NULL,
	item27 double DEFAULT NULL,
	item28 double DEFAULT NULL,
item29 double DEFAULT NULL,
	item30 double DEFAULT NULL,
	item31 double DEFAULT NULL,
	item32 double DEFAULT NULL,
item33 double DEFAULT NULL,
	item34 double DEFAULT NULL,
	item35 double DEFAULT NULL,
	item36 double DEFAULT NULL,
item37 double DEFAULT NULL,
	item38 double DEFAULT NULL,
	item39 double DEFAULT NULL,
	item40 double DEFAULT NULL,
item41 double DEFAULT NULL,
	item42 double DEFAULT NULL,
	item43 double DEFAULT NULL,
	item44 double DEFAULT NULL,
item45 double DEFAULT NULL,
	item46 double DEFAULT NULL,
item47 double DEFAULT NULL,
	item48 double DEFAULT NULL,
	item49 double DEFAULT NULL,
	item50 double DEFAULT NULL,
 PRIMARY KEY (sensorDataID)
);
DROP TABLE IF EXISTS XG_YG_BT;
CREATE TABLE XG_YG_BT(
	sensorDataID bigint(20) NOT NULL AUTO_INCREMENT,
	sourceAddr int(11) DEFAULT NULL,
	groupAddr int(11) DEFAULT NULL,
	samplingTime bigint(20) DEFAULT NULL,
	item1 double DEFAULT NULL,
	item2 double DEFAULT NULL,
 PRIMARY KEY (sensorDataID)
);
DROP TABLE IF EXISTS XG_WS_BT;
CREATE TABLE XG_WS_BT(
	sensorDataID bigint(20) NOT NULL AUTO_INCREMENT,
	sourceAddr int(11) DEFAULT NULL,
	groupAddr int(11) DEFAULT NULL,
	samplingTime bigint(20) DEFAULT NULL,
	item1 double DEFAULT NULL,
	item2 double DEFAULT NULL,
	item3 double DEFAULT NULL,
	item4 double DEFAULT NULL,
 PRIMARY KEY (sensorDataID)
);
DROP TABLE IF EXISTS XG_SJ_BT;
CREATE TABLE XG_SJ_BT(
	sensorDataID bigint(20) NOT NULL AUTO_INCREMENT,
	sourceAddr int(11) DEFAULT NULL,
	groupAddr int(11) DEFAULT NULL,
	samplingTime bigint(20) DEFAULT NULL,
	item1 double DEFAULT NULL,
	item2 double DEFAULT NULL,
 PRIMARY KEY (sensorDataID)
);
DROP TABLE IF EXISTS XG_MC_BT;
CREATE TABLE XG_MC_BT(
	sensorDataID bigint(20) NOT NULL AUTO_INCREMENT,
	sourceAddr int(11) DEFAULT NULL,
	groupAddr int(11) DEFAULT NULL,
	samplingTime bigint(20) DEFAULT NULL,
	item1 double DEFAULT NULL,
	item2 double DEFAULT NULL,
 PRIMARY KEY (sensorDataID)
);
DROP TABLE IF EXISTS XG_SW_BT;
CREATE TABLE XG_SW_BT(
	sensorDataID bigint(20) NOT NULL AUTO_INCREMENT,
	sourceAddr int(11) DEFAULT NULL,
	groupAddr int(11) DEFAULT NULL,
	samplingTime bigint(20) DEFAULT NULL,
	item1 double DEFAULT NULL,
	item2 double DEFAULT NULL,
 PRIMARY KEY (sensorDataID)
);
DROP TRIGGER IF EXISTS sensorDataUpdate;
CREATE TRIGGER sensorDataUpdate AFTER INSERT ON sensordata FOR EACH ROW begin
	declare tmpType varchar(100);
	select sensorType into tmpType
		 from SENSORTYPE
	where SourceAddr=new.SourceAddr and GroupAddr=new.GroupAddr limit 1;

	if strcmp(tmpType,'XG_TS_BT')=0 then
		insert into XG_TS_BT (SourceAddr,GroupAddr,SamplingTime,Item1,Item2,Item3,Item4)
			values(new.SourceAddr,new.GroupAddr,new.SamplingTime,new.Item1,new.Item2,new.Item3,new.Item4);
	end if;

	if strcmp(tmpType,'XG_YG_BT')=0 then
		insert into XG_YG_BT (SourceAddr,GroupAddr,SamplingTime,Item1,Item2)
			values(new.SourceAddr,new.GroupAddr,new.SamplingTime,new.Item1,new.Item2);
	end if;

	if strcmp(tmpType,'XG_WS_BT')=0 then
		insert into XG_WS_BT (SourceAddr,GroupAddr,SamplingTime,Item1,Item2,Item3,Item4)
			values(new.SourceAddr,new.GroupAddr,new.SamplingTime,new.Item1,new.Item2,new.Item3,new.Item4);
	end if;

	if strcmp(tmpType,'XG_SJ_BT')=0 then
		insert into XG_SJ_BT (SourceAddr,GroupAddr,SamplingTime,Item1,Item2)
			values(new.SourceAddr,new.GroupAddr,new.SamplingTime,new.Item1,new.Item2);
	end if;

	if strcmp(tmpType,'XG_MC_BT')=0 then
		insert into XG_MC_BT (SourceAddr,GroupAddr,SamplingTime,Item1,Item2)
			values(new.SourceAddr,new.GroupAddr,new.SamplingTime,new.Item1,new.Item2);
	end if;

	if strcmp(tmpType,'XG_SW_BT')=0 then
		insert into XG_SW_BT (SourceAddr,GroupAddr,SamplingTime,Item1,Item2)
			values(new.SourceAddr,new.GroupAddr,new.SamplingTime,new.Item1,new.Item2);
	end if;

end;
