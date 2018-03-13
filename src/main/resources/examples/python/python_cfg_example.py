class GalaxyBaseRunResponse(SuccessfulRunResponse):

def _wait_for_invocation(ctx, gi, history_id, workflow_id, invocation_id):

    def state_func():
        if _retry_on_timeouts(ctx, gi, lambda gi: has_jobs_in_states(gi, history_id, ["error", "deleted", "deleted_new"])):
            raise Exception("Problem running workflow, one or more jobs failed.")

        return _retry_on_timeouts(ctx, gi, lambda gi: gi.workflows.show_invocation(workflow_id, invocation_id))
    gi.timeout = 60
    return _wait_on_state(state_func)
