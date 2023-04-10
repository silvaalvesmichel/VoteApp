
echo "####### EXECUTE SCRIPTS SQS #######"

for script_name in /configs/sqs/*.sh; do
    bash "$script_name"
done

echo "####### FINISH EXECUTE SCRIPTS SQS #######"