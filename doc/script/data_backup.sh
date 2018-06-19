#!/bin/sh  
  
# config information 定义数据库连接  
db_host=10.1.1.32
db_username=root
db_password=ipharmacare
db_name=xy_sf
# 定义系统备份存储路径(脚本路径)  
backup_path="/mnt/systemcenter/xy/"



data_message="dataMessage.txt"
today=`date '+%Y-%m-%d'`
backup_dir="${backup_path}${today}"  
url=$(pwd)  

if [ ! -d "$backup_dir" ];then  
mkdir "$backup_dir"  
fi  

# 定义需要备份的数据库表数组  
backup_tables=( sf_opt_allergy sf_opt_audit_result sf_opt_diagnose sf_opt_electronic_medical 
				sf_opt_exam sf_opt_exam_item sf_opt_image sf_opt_msg sf_opt_msg_operation_record sf_opt_msg_statistic  
				sf_opt_patient_msg sf_opt_patient_msg_statistic sf_opt_patient_run_engine sf_opt_patient_run_engine_relation 
				sf_opt_product_engine_relation sf_opt_recipe sf_opt_recipe_item sf_opt_run_engine sf_opt_run_engine_relation 
				sf_opt_special_exam sf_out_patient sf_ipt_allergy sf_ipt_admission_record sf_ipt_audit_result_order 
				sf_ipt_diagnose sf_ipt_bacterial_report_item sf_ipt_bacterial_report sf_ipt_exam sf_ipt_exam_item sf_ipt_drug_sensitive_item 
				sf_ipt_drug_sensitive sf_ipt_image sf_ipt_operation sf_ipt_non_medical_order sf_ipt_order_cancel sf_ipt_patient sf_ipt_pathological_exam 
				sf_ipt_patient_engine_order sf_ipt_patient_msg sf_ipt_patient_run_engine sf_ipt_vital_sign sf_ipt_special_exam sf_ipt_progress_note 
				sf_ipt_medical_order sf_ipt_herb_medical_order  sf_ipt_herb_medical_order_item sf_ipt_audit_result sf_ipt_audit_result_item 
				sf_ipt_patient_run_engine_relation sf_ipt_product_engine_relation sf_ipt_product_related sf_ipt_vital_sign_related 
				)  
  
echo "================  begining The script   ================="  

cd $backup_dir  

#文件不存在则生成，存在则删除
if [ ! -f $data_message  ];then   
   touch $data_message
else
   rm -rf $data_message
fi

# 遍历备份的数据库表  
for t in ${backup_tables[@]};
do
  # 获取程序根目录记载的上次maxid 进行本次增量备份
  id=$(grep -w $t $url/$data_message | cut -d "," -f 2)

  if [ -z "$id" ]; then
     #echo "IS id  NULL"
     id=0
  fi 

  #echo "id ======:${id}"
  
  backup_file="${t}.dump"
  if [ ! -e "$backup_file" ];then  
     rm -f "$backup_file"  
  fi  
  # 最核心的就是这句话，使用mysqldump命令执行备份  
  mysqldump -h${db_host} -u $db_username -p${db_password}  --skip-extended-insert --replace  --no-create-info   --single-transaction --master-data=2 --order-by-primary  $db_name $t --where="id>${id} or id<0" > $backup_dir/$backup_file  
  
done  

echo "dump End at $url/${today}." 


# 遍历备份的数据库表备份文件，存储maxid文件
for u in ${backup_tables[@]};
do
  # 获取程序根目录记载的上次maxid 进行本次存储maxid比较
  id=$(grep -w  $u $url/$data_message | cut -d "," -f 2)

  if [ -z "$id" ]; then
     id=0
  fi

  row=$(grep REPLACE ${u}.dump | tail -n 1)

  if [ -z "$row" ]; then
     #echo "IS row NULL"
     maxid=0
  else
     idmsg=${row#*(}
     maxid=${idmsg%%,*}
  fi


  if [ $maxid -le $id ];then
      maxid=$id
 # else 
    # echo "TABLE ====$u     maxid ==== $maxid    id=======$id   END "
  fi
  #echo "$maxid"

  echo "$u,$maxid" >>$data_message

done

cp $data_message $url/

echo "data_message End at $url/$data_message"

cd ${backup_path}

zip -q -r ${today}.zip ${today} 

rm -rf ${today}

echo "================  End The script   ================="
