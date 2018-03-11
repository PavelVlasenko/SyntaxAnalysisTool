class GalaxyBaseRunResponse(SuccessfulRunResponse):

    def output_dataset_id(self, output):
        outputs = self.api_run_response["outputs"]
        output_id = output.get_id()
        output_dataset_id = None
        vlog("Looking for id [%s] in outputs [%s]" % (output_id, outputs))
        for output in outputs:
            if output["output_name"] == output_id:
                output_dataset_id = output["id"]
            else:
                output_dataset_id = output["ids"]

        return output_dataset_id