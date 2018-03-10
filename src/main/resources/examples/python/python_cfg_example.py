class GalaxyBaseRunResponse(SuccessfulRunResponse):

    def has_jobs_in_states(gi, history_id, states):
        params = {"history_id": history_id}
    jobs_url = gi._make_url(gi.jobs)
    jobs = Client._get(gi.jobs, params=params, url=jobs_url)

    target_jobs = [j for j in jobs if j["state"] in states]

    return len(target_jobs) > 0