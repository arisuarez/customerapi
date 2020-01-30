var app1 = new Vue({
    el: '#app-1',
    data: {
        loading: true,
        filter: {
            activeOnly: true,
            cityStartsWith: '',
            orderByCreationDate: false,
            orderType: 'asc'
        },
        cities: [],
        users: [],
        user: {
            id: 0,
            name: null,
            surname: null,
            email: null,
            city: null,
            birthdate: null,
            active: true
        },
    },
    methods: {
        search: function () {
            console.log(this.filter);
//             let params = {}
//             if (this.filter.activeOnly) {
//                 params.activeOnly = 1
//             }
//             if (this.filter.cityStartsWith) {
//                 params.cityStartsWith = this.filter.cityStartsWith
//             }
//             if (this.filter.orderByCreationDate) {
//                 params.orderBy = 'createdAt'
//                 params.orderType = this.filter.orderType
//             }
//             console.log('Search', params)

//             var params1 = {params: {...this.filter, orderBy: (this.filter.orderByCreationDate ? 'createdAt' : null)}}
// console.log('pepe', params1);

            axios
                .get('/api/users', { params: this.filter })
                .then(response => {
                    console.log(response)
                    this.users = response.data.map(function (x) {
                        x.birthday = moment(String(x.birthday)).format('MM/DD/YYYY')
                        x.createdAt = moment(String(x.createdAt)).format('MM/DD/YYYY')
                        return x
                    })
                })
                .catch(error => {
                    console.log(error)
                    // TODO mostrar mensaje
                    this.errored = true
                })
                .finally(() => this.loading = false)
        },
        showModal: function() {
            $('#addNewUserModal').modal('show')
        },
        createUser: function() {
            axios.post('/api/users', this.user)
            .then(response => {
                console.log('Create response', response)
                // this.users = response.data.map(function (x) {
                //     x.birthday = moment(String(x.birthday)).format('MM/DD/YYYY')
                //     x.createdAt = moment(String(x.createdAt)).format('MM/DD/YYYY')
                //     return x
                // })
            })
            .catch(error => {
                console.log('Create user error', error)
                // TODO mostrar mensaje
                this.errored = true
            })
            .finally(() => this.loading = false)
        }
    },
    mounted() {
        this.search()
    }
})

